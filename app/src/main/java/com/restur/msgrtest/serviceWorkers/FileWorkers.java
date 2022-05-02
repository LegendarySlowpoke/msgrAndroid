package com.restur.msgrtest.serviceWorkers;

import android.content.Context;

import com.restur.msgrtest.consts.ApplicationData;
import com.restur.msgrtest.exceptions.FileWorkerException;
import com.restur.msgrtest.models.ModelChat;
import com.restur.msgrtest.models.ModelMessage;
import com.restur.msgrtest.models.ModelUser;

import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileWorkers {

    public static void writeFile(Object objectToWrite, String filename) {
        Context context = ApplicationData.getAppContext();
        try {
            //Check if file exists
            File fileToSave = new File(ApplicationData.getAppContext().getFilesDir(), filename);
            if (!fileToSave.exists()) {
                fileToSave.createNewFile();
            }
            //Writing file
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(objectToWrite);
            oos.flush();

            byte[] fileContent = bos.toByteArray();
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(fileContent);

            fos.close();
            oos.close();
            bos.close();
            System.out.println("=====================================================\n" +
                    "File written! " + filename +
                    "\n=====================================================");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readFileObject(String filename) throws FileWorkerException {
        Context context = ApplicationData.getAppContext();
        FileInputStream fis;
        try {
            //Check if file exists
            if (!(new File(ApplicationData.getAppContext().getFilesDir(), filename)).exists()) {
                throw new FileWorkerException("File not found!");
            }
            //Reading file
            fis = context.openFileInput(filename);

            byte[] readIncome = new byte[fis.available()];
            for (int i = 0; i < readIncome.length; i++) {
                readIncome[i] = (byte)fis.read();
            }
            ByteArrayInputStream bis = new ByteArrayInputStream(readIncome);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object readObject = ois.readObject();
            System.out.println(readObject.toString());
            return readObject;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileWorkerException("Unexpected exception! " + e.getMessage());
        }
    }

    //todo finish this: should read JSON array
    public static List<Object> readFileArray(String filename) throws FileWorkerException {
        Context context = ApplicationData.getAppContext();
        FileInputStream fis;
        try {
            //Check if file exists
            if (!(new File(ApplicationData.getAppContext().getFilesDir(), filename)).exists()) {
                throw new FileWorkerException("File not found!");
            }
            //Reading file
            fis = context.openFileInput(filename);

            byte[] readIncome = new byte[fis.available()];
            for (int i = 0; i < readIncome.length; i++) {
                readIncome[i] = (byte)fis.read();
            }
            ByteArrayInputStream bis = new ByteArrayInputStream(readIncome);
            ObjectInputStream ois = new ObjectInputStream(bis);
            JSONArray readObject = (JSONArray) ois.readObject();
            List<Object> objectList = new ArrayList<>();
            for (Object object : objectList) {
                objectList.add(object);
            }

            printInfoList(objectList);
            System.out.println(readObject.toString());
            return objectList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileWorkerException("Unexpected exception! " + e.getMessage());
        }
    }

    private static void printInfoList(List<Object> objs) {
        if (objs != null) {
            if (objs.size() > 0) {
                String type = "";
                try {
                    ModelUser user = (ModelUser) objs.get(0);
                    type = "ModelUser";
                } catch (Exception e) {
                }
                try {
                    ModelChat chat = (ModelChat) objs.get(0);
                    type = "ModelChat";
                } catch (Exception e) {
                }
                try {
                    ModelMessage message = (ModelMessage) objs.get(0);
                    type = "ModelMessage";
                } catch (Exception e) {
                }
                if (type.equals("")) {
                    System.out.println("\t\t\tFileWorker printInfo: Read object length is " +
                            objs.size() + ", object type is unknown");
                } else {
                    System.out.println("\t\t\tFileWorker printInfo: Read object length is " +
                            objs.size() + ", object type is " + type);
                }
            } else {
                System.out.println("\t\t\tFileWorker printInfo: Read object length is 0");
            }
        } else {
            System.out.println("\t\t\tFileWorker printInfo: Read object list is null!");
        }

    }
}
