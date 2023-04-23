package Service;

import Models.Client;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObserveFolder implements Runnable {

    private final WatchService watcher;
    private final Map<WatchKey, Path> key_path; // chưa tất cả dir đang được quan sát
    private AtomicBoolean running = new AtomicBoolean(false);
    private Client client;

    public ObserveFolder(String path, Client client) throws IOException {
        this.client = client;
        this.watcher = FileSystems.getDefault().newWatchService();
        this.key_path = new HashMap<>();

        registerDirectory(Paths.get(path));
    }

    // đăng ký sự kiện lắng nghe cho một đường dẫn cụ thể
    private void registerDirectory(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        key_path.put(key, dir);
    }

    // đăng ký sự kiến quan sát cho các folder con
    private void walkAndRegisterDirectories(final Path start) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                registerDirectory(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    void processEvent() throws InterruptedException {
        running.set(true);
        WatchKey key = null;
        while (running.get() && (key = watcher.take()) != null) {
            Path dir = key_path.get(key);

            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                @SuppressWarnings("rawtypes")
                WatchEvent.Kind kind = event.kind();

                @SuppressWarnings("unchecked")
                Path name_file = ((WatchEvent<Path>) event).context();
                Path child_file = dir.resolve(name_file);

                String cur_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());

                // khi một thư mục mới được tạo ra thì ta thiết lập sự kiện cho thư mục mới này
                if (kind == ENTRY_CREATE) {
                    this.client.addNotify(client.getName() + " - " + cur_time + " - " + " THAO TÁC TẠO TẠI: " + child_file);
                    try {
                        if (Files.isDirectory(child_file)) {
                            walkAndRegisterDirectories(child_file);
                        }
                    } catch (IOException x) {
                    }
                } else if (kind == ENTRY_MODIFY) {
                    this.client.addNotify(client.getName() + " - " + cur_time + " - " + " THAO TÁC SỬA TẠI: " + child_file);
                } else if (kind == ENTRY_DELETE) {
                    this.client.addNotify(client.getName() + " - " + cur_time + " - " + " THAO TÁC XÓA TẠI: " + child_file);
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                key_path.remove(key);

                if (key_path.isEmpty()) {
                    break;
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            processEvent();
        } catch (InterruptedException ex) {
            Logger.getLogger(ObserveFolder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        running.set(false);
    }
}
