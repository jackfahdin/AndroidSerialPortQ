package com.jackfahdin.AndroidSerialPortQ;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jackfahdin.SerialPortConfig.SerialHelper;
import com.jackfahdin.SerialPortConfig.bean.ComBean;
import com.jackfahdin.SerialPortJNI.SerialPortFinder;
import com.jackfahdin.adapter.SpAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Spinner spinnerSerialPort;
    private EditText editTextInput;
    private Button buttonSend;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private SerialPortFinder serialPortFinder;
    private SerialHelper serialHelper;
    private Spinner spinnerBaudRate;
    private Button buttonOpen;
//    todo List
    private Spinner spinnerDataBits;
    private Spinner spinnerParity;
    private Spinner spinnerStopBits;
    private Spinner spinnerFlowCon;

    @Override
    protected void onDestroy() {
        serialHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        spinnerSerialPort = findViewById(R.id.sp_serial);
        editTextInput = findViewById(R.id.ed_input);
        buttonSend = findViewById(R.id.btn_send);
        spinnerBaudRate = findViewById(R.id.sp_baudrate);
        buttonOpen = findViewById(R.id.btn_open);

        radioGroup = findViewById(R.id.radioGroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);

        spinnerDataBits = findViewById(R.id.sp_databits);
        spinnerParity = findViewById(R.id.sp_parity);
        spinnerStopBits = findViewById(R.id.sp_stopbits);
        spinnerFlowCon = findViewById(R.id.sp_flowcon);

        // todo

        serialPortFinder = new SerialPortFinder();
        serialHelper = new SerialHelper("/dev/ttyS1", 115200) {


            @Override
            protected void onDataReceived(ComBean paramComBean) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // todo 接收数据
                    }
                });
            }
        };

        final String[] serialPortList = serialPortFinder.getAllDevices();
        final String[] baudRateList = new String[]{"9600", "115200", "1500000"};
        final String[] dataBitsList = new String[]{"8", "7", "6", "5"};
        final String[] parityList = new String[]{"None", "Odd", "Even"};
        final String[] stopBitsList = new String[]{"1", "2"};
        final String[] flowConList = new String[]{"NONE", "RTS/CTS", "XON/XOFF"};

        SpAdapter spAdapter = new SpAdapter(this);
        spAdapter.setDatas(serialPortList);
        spinnerSerialPort.setAdapter(spAdapter);

        spinnerSerialPort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serialHelper.close();
                serialHelper.setPort(serialPortList[position]);
                buttonOpen.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpAdapter spAdapterBaudRate = new SpAdapter(this);
        spAdapterBaudRate.setDatas(baudRateList);
        spinnerBaudRate.setAdapter(spAdapterBaudRate);
        spinnerBaudRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serialHelper.close();
                serialHelper.setBaudRate(baudRateList[position]);
                buttonOpen.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpAdapter spAdapterDataBits = new SpAdapter(this);
        spAdapterDataBits.setDatas(dataBitsList);
        spinnerDataBits.setAdapter(spAdapterDataBits);
        spinnerDataBits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serialHelper.close();
                serialHelper.setDataBits(Integer.parseInt(dataBitsList[position]));
                buttonOpen.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpAdapter spAdapterParity = new SpAdapter(this);
        spAdapterParity.setDatas(parityList);
        spinnerParity.setAdapter(spAdapterParity);
        spinnerParity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serialHelper.close();
                serialHelper.setParity(position);
                buttonOpen.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpAdapter spAdapterStopBits = new SpAdapter(this);
        spAdapterStopBits.setDatas(stopBitsList);
        spinnerStopBits.setAdapter(spAdapterStopBits);
        spinnerStopBits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serialHelper.close();
                serialHelper.setStopBits(Integer.parseInt(stopBitsList[position]));
                buttonOpen.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpAdapter spAdapterFlowCon = new SpAdapter(this);
        spAdapterFlowCon.setDatas(flowConList);
        spinnerFlowCon.setAdapter(spAdapterFlowCon);
        spinnerFlowCon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serialHelper.close();
                serialHelper.setFlowCon(position);
                buttonOpen.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    serialHelper.open();
                    buttonOpen.setText("关闭");
                } catch (IOException | SecurityException e) {
                    Toast.makeText(MainActivity.this, getString(R.string.tips_cannot_be_opened, e.getMessage()), Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                // todo 发送数据
            }
        });
    }
}