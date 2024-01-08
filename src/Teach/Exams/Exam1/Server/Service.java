package Teach.Exams.Exam1.Server;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class Service {

    private static String pathname = "src/Teach/Exams/Exam1/DATA/thisinh.dat";
    private static Service instance;

    private Map<Integer, Long> map;

    private RandomAccessFile raf;

    public boolean isValidCandidate(Candidate candidate) {
        return candidate.isValidAge();
    }

    private Service() throws IOException {
        File file = new File(pathname);
        this.map = new HashMap<Integer, Long>();
        if (!file.exists()) {
            file.createNewFile();
            raf = new RandomAccessFile(file, "rw");
            // Ghi header
            raf.writeInt(0);
        } else raf = new RandomAccessFile(file, "rw");
    }

    public static Service getInstance() throws IOException {
        if(instance == null)
            instance = new Service();
        return instance;
    }

    private char[] toCharArray(String str, int length) {
        char[] strChars = new char[length];
        str.getChars(0, str.length(), strChars, 0);
        return strChars;
    }

    private byte[] charToByte(char[] chars) {
        byte[] bytes = new byte[chars.length];
        for(int i = 0; i < bytes.length; i++)
            bytes[i] = (byte)chars[i];
        return bytes;
    }

    private String bytesToString(byte[] bytes) {
        return new String(bytes);
    }

    public void insertCandidate(Candidate candidate) throws IOException {
        // Ghi đè lại size của header
        raf.seek(0);
        int size = raf.readInt();
        size++;
        raf.seek(0);
        raf.writeInt(size);

        raf.seek(raf.length());

        candidate.setId(size);

        // Dịch string ra byte[]
        byte[] nameBytes = charToByte(toCharArray(candidate.getName(), 25));
        byte[] birthdayBytes = charToByte(toCharArray(candidate.getBirthday(), 10));
        byte[] addressBytes = charToByte(toCharArray(candidate.getAddress(), 25));

        // Ghi thông tin thí sinh
        long pointer = raf.getFilePointer();
        raf.writeInt(candidate.getId());
        raf.write(nameBytes);
        raf.write(birthdayBytes);
        raf.write(addressBytes);
        byte[] bytes = new byte[100000];
        raf.write(bytes);

        map.put(candidate.getId(), pointer);
    }

    public void writeFile(int id, byte[] buffer) throws IOException {
        long pointer = map.get(id);
        pointer += 4 + 25 + 10 + 25;
        raf.seek(pointer);
        raf.write(buffer);
    }

    public void update(int id, String address) throws IOException {
        long pointer = map.get(id);
        pointer += 4 + 25 + 10;
        raf.seek(pointer);
        byte[] addressBytes = charToByte(toCharArray(address, 25));
        raf.write(addressBytes);

    }

    public boolean isExist(int id) {
        return map.containsKey(id);
    }

    public String getUserData(int id) throws IOException {
        long pointer = map.get(id);
        raf.seek(pointer);

        byte[] nameBytes = new byte[25];
        byte[] birthdayBytes = new byte[10];
        byte[] addressBytes = new byte[25];

        int _id = raf.readInt();
        raf.read(nameBytes);
        raf.read(birthdayBytes);
        raf.read(addressBytes);
        byte[] bytes = new byte[100000];
        raf.read(bytes);
        int size = 0;
        for(int i = 0; i < bytes.length; i++)
            if(bytes[i] != 0)
                size++;
            else break;
        String name = bytesToString(nameBytes);
        String birthday = bytesToString(birthdayBytes);
        String address = bytesToString(addressBytes);
        return "ID = " + _id + ", NAME = " + name + ", BIRTHDAY = " + birthday + ", ADDRESS = " + address + ", FILE SIZE = " + size;
    }
}
