package org.ocram.reflection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class FindResourceInJarTest extends ScratchBaseTest {

    @Test
    public void findResourceTest() throws Exception {
        // local
        String path = "/org/ocram/properties/pkg.properties";
        internalLoadResources(path, false);

        path = "/org/ocram/properties/";
        internalLoadResources(path, true);

        // file
        String content = "test file created by " + this.getClass().getSimpleName();

        final String baseTempPath = System.getProperty("java.io.tmpdir");
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".tst");
        tempFile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(content.getBytes());
        fos.close();
        internalLoadResources(tempFile.getAbsolutePath(), false);

        File tempDir = new File(baseTempPath + "/" + UUID.randomUUID().toString());
        tempDir.mkdir();
        tempDir.deleteOnExit();
        tempFile = new File(tempDir.getAbsolutePath() + "/" + UUID.randomUUID().toString() + ".tst");
        fos = new FileOutputStream(tempFile);
        fos.write(content.getBytes());
        fos.close();
        internalLoadResources(tempDir.getAbsolutePath(), true);
    }

    private List<KJarResource> internalLoadResources(String path, boolean fromDir) throws Exception {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        List<KJarResource> output = new ArrayList<KJarResource>();
        URL url = this.getClass().getResource(path);
        if (fromDir) {
            if (url == null) {
                File folder = new File(path);
                if (folder.exists()) {
                    for (File folderFile : folder.listFiles()) {
                        if (folderFile.isDirectory()) {
                            continue;
                        }
                        String content = convertFileToString(new FileInputStream(folderFile));
                        output.add(new KJarResource(folderFile.getName(), content));
                    }
                } else {
                    throw new IllegalArgumentException("Directory + '" + path + "' does not exist.");
                }
            } else if ("file".equals(url.getProtocol())) {
                File folder = new File(url.toURI());
                if (folder.isDirectory()) {
                    for (File folderFile : folder.listFiles()) {
                        if (folderFile.getName().endsWith(".class")) {
                            continue;
                        }
                        String content = convertFileToString(new FileInputStream(folderFile));
                        output.add(new KJarResource(folderFile.getName(), content));
                    }
                } else {
                    throw new IllegalStateException("'" + path + "' is not an existing directory.");
                }
            } else if ("jar".equals(url.getProtocol())) {
                String urlString = url.toExternalForm();
                int jarPathIndex = urlString.lastIndexOf(".jar!") + 4;
                String resourcePath = urlString.substring(jarPathIndex + 1);
                if (resourcePath.startsWith("/")) {
                    resourcePath = resourcePath.substring(1);
                }
                int depth = resourcePath.split(File.separator).length + 1;

                String jarUrlString = urlString.substring("jar:".length(), jarPathIndex);
                url = new URL(jarUrlString);
                ZipInputStream zip = new ZipInputStream(url.openStream());

                ZipEntry ze = zip.getNextEntry();
                while (ze != null) {
                    String name = ze.getName();
                    if (name.startsWith(resourcePath) && !name.endsWith(File.separator)
                            && (name.split(File.separator).length == depth)) {
                        String shortName = name.substring(name.lastIndexOf("/") + 1);
                        String content = convertFileToString(zip);
                        output.add(new KJarResource(shortName, content));
                    }
                    ze = zip.getNextEntry();
                }
            } else {
                throw new IllegalArgumentException("Unable to find resource directory '" + path + "'");
            }
        } else {
            InputStream is = this.getClass().getResourceAsStream(path);
            if (is == null) {
                is = new FileInputStream(new File(path));
            }
            String content = convertFileToString(is);
            String name = path.substring(path.lastIndexOf("/") + 1);
            output.add(new KJarResource(name, content));
        }
        return output;
    }

    private static String convertFileToString(InputStream in) {
        InputStreamReader input = new InputStreamReader(in);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter output = new OutputStreamWriter(baos);
        char[] buffer = new char[4096];
        int n = 0;
        try {
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toString();
    }

    private static class KJarResource {
        public String name;
        public String content;

        public KJarResource(String name, String content) {
            this.content = content;
            this.name = name;
        }
    }
}
