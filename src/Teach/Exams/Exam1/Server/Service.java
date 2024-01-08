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

    public void insertCandidate(Candidate candidate) throws IOException {
        // Ghi đè lại size của header
        raf.seek(0);
        int size = raf.readInt();
        size++;
        raf.seek(0);
        raf.writeInt(size);

        raf.seek(raf.length());

        candidate.setId(size);

        // Ghi thông tin thí sinh
        long pointer = raf.getFilePointer();
        raf.writeInt(candidate.getId());
        raf.writeUTF(candidate.getName());
        raf.writeUTF(candidate.getBirthday());
        raf.writeUTF(candidate.getAddress());
        byte[] bytes = new byte[100000];
        raf.write(bytes);

        map.put(candidate.getId(), pointer);
    }

    public void writeFile(int id, byte[] buffer) throws IOException {
        long pointer = map.get(id);
        raf.seek(pointer);

        raf.readInt();
        raf.readUTF();
        raf.readUTF();
        raf.readUTF();

        raf.write(buffer);
    }

    public void update(int id, String address) throws IOException {
        long pointer = map.get(id);
        raf.seek(pointer);
        
        int _id = raf.readInt();
        String name = raf.readUTF();
        String birthday = raf.readUTF();
        String oldAddress = raf.readUTF();

        byte[] bytes = new byte[100000];
        raf.read(bytes);

        Candidate candidate = new Candidate(name, birthday, address);
        candidate.setId(id);

        // Ghi dữ liệu mới
        raf.seek(pointer);
        raf.writeInt(candidate.getId());
        raf.writeUTF(candidate.getName());
        raf.writeUTF(candidate.getBirthday());
        raf.writeUTF(candidate.getAddress());
        raf.write(bytes);

    }

    public boolean isExist(int id) {
        return map.containsKey(id);
    }

    public String getUserData(int id) throws IOException {
        long pointer = map.get(id);
        raf.seek(pointer);

        int _id = raf.readInt();
        String name = raf.readUTF();
        String birthday = raf.readUTF();
        String address = raf.readUTF();

        byte[] bytes = new byte[100000];
        raf.read(bytes);
        int size = 0;
        for(int i = 0; i < bytes.length; i++)
            if(bytes[i] != 0)
                size++;
            else break;
        return "ID = " + _id + ", NAME = " + name + ", BIRTHDAY = " + birthday + ", ADDRESS = " + address + ", FILE SIZE = " + size;
    }
}
