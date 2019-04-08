package com.example.attendancemanagementsystem.Activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.attendancemanagementsystem.Model.StudentItem;

import android.widget.Toast;

import com.example.attendancemanagementsystem.NFC.NdefMessageParser;
import com.example.attendancemanagementsystem.NFC.ParsedNdefRecord;
import com.example.attendancemanagementsystem.R;
import com.example.attendancemanagementsystem.Utils.AppConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.List;

public class ScanActivity extends AppCompatActivity {

    private Button finishAttendance, submitAttendance;
    private EditText studentID;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mDatabase;
    boolean flag1 = true;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "No NFC Available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        studentID = findViewById(R.id.student_id);
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        finishAttendance = findViewById(R.id.finish_attendance);
        submitAttendance = findViewById(R.id.submit_id);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseInstance.getReference();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy-HH");
        formattedDate = df.format(c);
        submitAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String studentIDS = studentID.getText().toString();
                sendData(studentIDS);
            }
        });

        finishAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendData(final String str) {
        mDatabase.child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag = false, loopFinished = false;
                int i = 0;
                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final StudentItem studentItem = dataSnapshot1.getValue(StudentItem.class);
                    i++;
                    if (studentItem.getStudentId().equalsIgnoreCase(str)) {
                        mDatabase.child("classes").child(AppConstants.classID).child("period").child(formattedDate).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.child(dataSnapshot1.getKey()).exists()) {
                                    mDatabase.child("classes").child(AppConstants.classID).child("period").child(formattedDate).child(dataSnapshot1.getKey()).setValue(studentItem.getStudentId());
                                    mDatabase.child("classes").child(AppConstants.classID).child("students").child(studentItem.getStudentId()).push().setValue(formattedDate);
                                } else {
                                    Toast.makeText(ScanActivity.this, "Attendance Already Taken", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("ScanActivity", "Failed to get firebase data");
                            }
                        });
                        flag = true;
                    }
                    if (dataSnapshot.getChildrenCount() == i)
                        loopFinished = true;
                }
                if (!flag && loopFinished) {
                    Intent intent = new Intent(ScanActivity.this, AddStudentActivity.class);
                    intent.putExtra("studentEncryption", str);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ScanActivity", "Failed to get firebase data");
            }
        });
    }

    private void processID(final String str) {
        flag1 = true;

        mDatabase.child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (flag1) {
                    flag1 = false;
                    if (dataSnapshot.child(str).exists()) {
                        final StudentItem studentItem = dataSnapshot.child(str).getValue(StudentItem.class);
                        mDatabase.child("classes").child(AppConstants.classID).child("period").child(formattedDate).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.child(str).exists()) {
                                    mDatabase.child("classes").child(AppConstants.classID).child("period").child(formattedDate).child(str).setValue(studentItem.getStudentId());
                                    mDatabase.child("classes").child(AppConstants.classID).child("students").child(studentItem.getStudentId()).push().setValue(formattedDate);
                                } else {
                                    Toast.makeText(ScanActivity.this, "Attendance Already Taken", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("ScanActivity", "Failed to get firebase data");
                            }
                        });
                    } else {
                        Intent intent = new Intent(ScanActivity.this, AddStudentActivity.class);
                        intent.putExtra("studentEncryption", str);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ScanActivity", "Failed to get firebase data");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled()) showWirelessSettings();
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    private void showWirelessSettings() {
        Toast.makeText(this, "You Need to Enable NFC", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
    }

    @Override
    protected void onPause() {
        nfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++)
                    msgs[i] = (NdefMessage) rawMsgs[i];
            } else {
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
            }
            displayMsgs(msgs);
        }
    }


    private void displayMsgs(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;
        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        int size = records.size();
        ParsedNdefRecord record = records.get(0);
        String str = record.str();
        str = str.substring(1, str.length() - 1);
        str = str.replace("/", "");

        processID(str);
    }

    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(toHex(id)).append('\n');
        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
        sb.append("ID (dec): ").append(toDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";

                try {
                    MifareClassic mifareTag = MifareClassic.get(tag);

                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }
}
