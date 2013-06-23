package com.innovation.filewatcher;

import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import  com.innovation.util.*;

/**
 * Example to watch a directory (or tree) for changes to files.
 */

public class WatchDir implements Runnable{

    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    private Path dir;
    private boolean trace = false;
    
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();

    @Override
	public void run() {
		// TODO Auto-generated method stub
    	 
        //String logsLocation="c:\\test";

        // register directory and process its events
    	processEvents();
        
	}
    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }



    /**
     * Creates a WatchService and registers the given directory
     */
    public WatchDir(String logsLocation) throws IOException {
    	this.dir = Paths.get(logsLocation);
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();

        register(dir);


        // enable trace after initial registration
        this.trace = true;
    }

    /**
     * Process all events for keys queued to the watcher
     */
    void processEvents() {
        for (;;) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();
                

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                // print out event
               // System.out.format("%s: %s\n", event.kind().name(), child);
                Utility.fileTracker.put(child.toString(), dateFormat.format(new Date()));
               // System.out.println("fileTrackers"+Utility.fileTracker);


            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }


    public static void main(String[] args) throws IOException {
       

        
        String logsLocation="c:\\test";

        // register directory and process its events
      
        new WatchDir(logsLocation).processEvents();
    }

	
}