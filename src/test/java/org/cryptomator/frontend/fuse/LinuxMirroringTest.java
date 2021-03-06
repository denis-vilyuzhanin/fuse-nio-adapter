package org.cryptomator.frontend.fuse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class LinuxMirroringTest {

	public static void main(String[] args) throws IOException {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Enter path to the directory you want to mirror:");
			Path p = Paths.get(scanner.nextLine());
			int uid = (int) Files.getAttribute(p, "unix:uid");
			int gid = (int) Files.getAttribute(p, "unix:gid");
			System.out.println("Enter mount point:");
			Path m = Paths.get(scanner.nextLine());
			if (Files.isDirectory(p) && Files.isDirectory(m)) {
				try (FuseNioAdapter fs = AdapterFactory.createReadWriteAdapter(p)) {
					fs.mount(m, false, false, new String[]{"-ouid="+uid, "-ogid="+gid, "-oatomic_o_trunc"});
					System.out.println("Mounted successfully. Enter anything to stop the server...");
					System.in.read();
					fs.umount();
					System.out.println("Unmounted successfully. Exiting...");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.err.println("Invalid directory.");
			}
		}
	}

}
