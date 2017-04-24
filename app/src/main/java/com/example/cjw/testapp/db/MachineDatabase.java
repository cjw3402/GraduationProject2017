package com.example.cjw.testapp.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.cjw.testapp.BasicInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.database.sqlite.SQLiteDatabase.OPEN_READWRITE;

public class MachineDatabase extends SQLiteOpenHelper {

    private static final String TAG = "MachineDatabase";

    /**
     * 싱글톤 인스턴스
     */
    private static MachineDatabase database = null;

    /**
     * table name for MACHINE_INFO
     */
    public static final String TABLE_MACHINE_INFO = "MACHINE_INFO";

    /**
     * table name for TABLE_DOCUMENT_INFO
     */
    public static final String TABLE_DOCUMENT_INFO = "document_info";


    /**
     * version
     */
    private static int DATABASE_VERSION = 1;

    /**
     * SQLiteDatabase 인스턴스
     */
    private SQLiteDatabase db = null;

    /**
     * 컨텍스트 객체
     */
    private Context context;

    /**
     * 생성자
     */
    public MachineDatabase(Context context) {
        super(context, BasicInfo.DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        // 내부에 저장된 DB가 있는지 확인
        try {
            boolean checkDB = isCheckDB();
            Log.i(TAG, "DB Check: "+ checkDB);

            if (!checkDB) {   // DB가 없으면 assets의 DB를 프로그램 내부로 복사함
                copyDB(context);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 인스턴스 가져오기
     */
    public static MachineDatabase getInstance(Context context) {
        if (database == null) {
            database = new MachineDatabase(context);
        }

        return database;
    }

    /**
     * 데이터베이스 열기
     */
    public boolean open() {
        println("opening database [" + BasicInfo.DATABASE_NAME + "].");

        db = SQLiteDatabase.openDatabase(BasicInfo.DATABASE_FILE_Location, null, OPEN_READWRITE);

        if (db == null) {
            Log.d(TAG, "database open failed...\n");
            return false;
        }

//        Toast.makeText(context, "open success!", Toast.LENGTH_SHORT).show();

        return true;
    }

    /**
     * 데이터베이스 닫기
     */
    public void close() {
        println("closing database [" + BasicInfo.DATABASE_NAME + "].");
        db.close();

        database = null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        println("opened database [" + BasicInfo.DATABASE_NAME + "].");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        println("Upgrading database from version " + oldVersion + " to " + newVersion + ".");
    }

    public Cursor rawQuery(String SQL) {
        println("\nexecuteQuery called.\n");

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(SQL, null);
            println("cursor count : " + cursor.getCount());
        }
        catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
        }

        return cursor;
    }

    public boolean execSQL(String SQL) {
        println("\nexecute called.\n");

        try {
            Log.d(TAG, "SQL: " + SQL);
            db.execSQL(SQL);
        }
        catch (Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
            return false;
        }

        return true;
    }

    private void println(String msg) {
        Log.d(TAG, msg);
    }

    // 내부에 저장된 DB가 있는지 확인하기
    private boolean isCheckDB(){
        File file = new File(BasicInfo.DATABASE_FILE_Location);

        if (file.exists()) {
            return true;
        }

        return false;
    }

    // assets의 /db/xxxx.db 파일을 설치된 프로그램의 내부 DB공간으로 복사하기
    private void copyDB(Context context){
        Log.d("MiniApp", "copyDB");

        AssetManager manager = context.getAssets();

        File folder = new File(BasicInfo.DATABASE_FOLDER_LOCATION);
        File file = new File(BasicInfo.DATABASE_FILE_Location);

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            InputStream is = manager.open("db/" + BasicInfo.DATABASE_NAME);
            BufferedInputStream bis = new BufferedInputStream(is);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }

            bos.flush();

            bos.close();
            fos.close();
            bis.close();
            is.close();

        }
        catch (IOException e) {
            Log.e("ErrorMessage : ", e.getMessage());
        }

    }

}
