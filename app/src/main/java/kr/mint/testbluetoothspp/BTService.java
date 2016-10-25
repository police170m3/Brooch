package kr.mint.testbluetoothspp;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class BTService extends Service {
    private BluetoothAdapter mAdapter;
    private Context _context;

    public static String callrecv_min = null;
    public static String callrecv_avg = null;
    public static String callrecv_max = null;
    public static byte[] configure3 =      {-1, 85, 8, 0, 3, 60, 3, 3, 3, 10};

    public static String brooch_DB = null;
    public static boolean brooch_safe = false;
    public static boolean config_check = false;//ninny

    public static BluetoothSocket mmSocket;
    public static InputStream mmInStream;
    public static OutputStream mmOutStream;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public BTService(Context $context) {
        super();
        _context = $context;
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 주소로 연결하기
     *
     * @param $address mac address
     */
    public void connect(String $address) {
        BluetoothDevice device = mAdapter.getRemoteDevice($address);
        connect(device);
    }

    public void connect(BluetoothDevice $device) {
        ConnectThread thread = new ConnectThread($device);
        thread.start();
    }

    private void manageConnectedSocket(BluetoothSocket $socket) {
        Log.i("BTService.java | manageConnectedSocket", "|==" + $socket.getRemoteDevice().getName() + "|" + $socket.getRemoteDevice().getAddress());
        PreferenceUtil.putLastRequestDeviceAddress($socket.getRemoteDevice().getAddress());
        mAdapter.cancelDiscovery();
        ConnectedThread thread = new ConnectedThread($socket);
        thread.start();
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket, because mmSocket is final
            BluetoothSocket tmp = null;
//         mmDevice = device;
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }


        public void run() {
            // Cancel discovery because it will slow down the connection
            mAdapter.cancelDiscovery();
            try {
                // Connect the device through the socket. This will block until it succeeds or throws an exception
                mmSocket.connect();
            } catch (Exception e1) {
                Log.e("BTService.java | run", "|==" + "connect fail" + "|");

                e1.printStackTrace();
                // Unable to connect; close the socket and get out
                try {
                    if (mmSocket.isConnected())
                        mmSocket.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                return;
            }
            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }


        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    public class ConnectedThread extends Thread {

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // Get the input and output streams, using temp objects because member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }



        public void run() {
            //byte[] buffer = new byte[1024]; // buffer store for the stream
            //int bytes; // bytes returned from read()
            byte[] readBuffer = new byte[1024];
            int readBufferPosition = 0;
            final byte delimiter = 10;

            while (true) {
                try {
                    // Read from the InputStream
                    //bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI Activity
//               mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    //Log.i("BTService.java | run", "|==" + bytes2String(buffer, bytes) + "|");

                    int bytesAvailable = mmInStream.available();

                    if (bytesAvailable > 0) {
                        final byte[] packetBytes = new byte[bytesAvailable];
                        mmInStream.read(packetBytes);

                        for (int i = 0; i < bytesAvailable; i++) {
                            byte b = packetBytes[i];

                            if (b == delimiter) {
                                if (readBuffer[0] == -1) {
                                    if (readBuffer[1] == 85) {
                                        if (readBuffer[4] == 8) {
                                            brooch_DB = String.valueOf((int) readBuffer[5]);
                                            Intent intent = new Intent("kr.mint.bluetooth.receive");
                                            intent.putExtra("signal", bytes2String(readBuffer[5]));
                                            Log.d("00000000000000000000000000000000000000-----", bytes2String(readBuffer[5]));
                                            brooch_safe = false;
//                                            BluetoothSignalReceiver.signal =  Integer.parseInt(bytes2String(readBuffer[5]));
                                            _context.sendBroadcast(intent);
                                            readBufferPosition = 0;
                                        } else if (readBuffer[4] == 7) {
                                            brooch_safe = true;
                                            Intent intent = new Intent("kr.mint.bluetooth.receive");
                                            intent.putExtra("signal_safe", bytes2String(readBuffer[5]));
                                            Log.d("00000000000000000000000000000000000000-----", bytes2String(readBuffer[5]));
//                                            BluetoothSignalReceiver.signal_safe = Integer.parseInt(bytes2String(readBuffer[5]));
                                            _context.sendBroadcast(intent);
                                            readBufferPosition = 0;
                                            // safe 모드 (safe 모듈 실행)
                                        } else if (readBuffer[4] == 2) {
                                            // 녹음 데이터 min, avg, max
                                            callrecv_min = String.valueOf((int) readBuffer[5]);
                                            callrecv_avg = String.valueOf((int) readBuffer[6]);
                                            callrecv_max = String.valueOf((int) readBuffer[7]);
//                                            Intent intent = new Intent("kr.mint.bluetooth.receive"); //ninny
//                                            intent.putExtra("signal", "min, avg, max save");
//                                            _context.sendBroadcast(intent);   //ninny
                                            readBufferPosition = 0;
                                        }
                                    }
                                }

                            } else {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }


    }

    private String bytes2String(byte b) {
        ArrayList<StringBuffer> result = new ArrayList<>();
//        ArrayList<String> result = new ArrayList<String>();
        //for (int i = 0; i < count; i++) {

 //       String myInt = String.valueOf((int) b);
        StringBuffer myInt = new StringBuffer(String.valueOf((int) b));   //stringbuffer로 변경 ninny 10월21일 5시46분
//                String myInt =  Integer.toHexString((int) (b[i] & 0xFF));
        result.add(myInt);

        // }
        return TextUtils.join("-", result);
    }

    /* Call this from the main Activity to send data to the remote device */
    public static void writesSelect(int num) throws IOException {

        byte[] call1 =           {-1, 85, 8, 0, 1,  0, 0, 0, 0, 10};
        byte[] callrecv2 =       {-1, 85, 8, 0, 2,  0, 0, 0, 0, 10};
   //     byte[] configure3 =      {-1, 85, 8, 0, 3, 60, 3, 3, 3, 10};
        byte[] cation4 =         {-1, 85, 8, 0, 4,  0, 0, 0, 0, 10};
        byte[] serious5 =        {-1, 85, 8, 0, 5,  0, 0, 0, 0, 10};
        byte[] warning6 =        {-1, 85, 8, 0, 6,  0, 0, 0, 0, 10};
        byte[] callangry7 =      {-1, 85, 8, 0, 9,  0, 0, 0, 0, 10};                         //ninny
        byte[] broothstates8 =      {-1, 85, 8, 0, 7,  0, 0, 0, 0, 10};                         //ninny

        switch (num) {
            case 1:
                for (int i = 0; i <= 9; i++) {
                    mmOutStream.write(call1[i]);
                    Log.d("hello~~~~~~~~~~~", "mmOutStream.write(call1[i]);");
                }
                break;
            case 2:
                for (int i = 0; i <= 9; i++) {
                    mmOutStream.write(callrecv2[i]);
                    Log.d("hello~~~~~~~~~~~", "mmOutStream.write(callrecv2[i]);");
                }
                break;
            case 3:
                for (int i = 0; i <= 9; i++) {
                    mmOutStream.write(configure3[i]);
                    Log.d("hello~~~~~~~~~~~", "mmOutStream.write(configure3[i]);");
                }
                break;
            case 4:
                for (int i = 0; i <= 9; i++) {
                    mmOutStream.write(cation4[i]);
                    Log.d("hello~~~~~~~~~~~", "mmOutStream.write(cation4[i]);");
                }
                break;
            case 5:
                for (int i = 0; i <= 9; i++) {
                    mmOutStream.write(serious5[i]);
                    Log.d("hello~~~~~~~~~~~", "mmOutStream.write(serious5[i]);");
                }
                break;
            case 6:
                for (int i = 0; i <= 9; i++) {
                    mmOutStream.write(warning6[i]);
                    Log.d("hello~~~~~~~~~~~", "mmOutStream.write(warning6[i]);");
                }
                break;
            case 9:                         //ninny
                for (int i = 0; i <= 9; i++) {
                    mmOutStream.write(callangry7[i]);
                    Log.d("hello~~~~~~~~~~~", "mmOutStream.write(callangry7[i]);");
                }
                break;
            case 10:
                for (int i = 0; i <= 9; i++) {
                    mmOutStream.write(broothstates8[i]);
                    Log.d("hello~~~~~~~~~~~", "mmOutStream.write(broothstates8[i]);");
                }
                break;                         //ninny
        }
    }

    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
        }
    }

    /* Call this from the main Activity to shutdown the connection */
    public static void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }
}




/*


package kr.mint.testbluetoothspp;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class BTService extends Service {
    private BluetoothAdapter mAdapter;
    private Context _context;

    public static String callrecv_min = null;
    public static String callrecv_avg = null;
    public static String callrecv_max = null;
    public static String brooch_DB = null;
    public static String brooch_safe = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public BTService(Context $context) {
        super();
        _context = $context;
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    */
/**
     * 주소로 연결하기
     *
     * @param $address mac address
     *//*

    public void connect(String $address) {
        BluetoothDevice device = mAdapter.getRemoteDevice($address);
        connect(device);
    }

    public void connect(BluetoothDevice $device) {
        ConnectThread thread = new ConnectThread($device);
        thread.start();
    }

    private void manageConnectedSocket(BluetoothSocket $socket) {
        Log.i("BTService.java | manageConnectedSocket", "|==" + $socket.getRemoteDevice().getName() + "|" + $socket.getRemoteDevice().getAddress());
        PreferenceUtil.putLastRequestDeviceAddress($socket.getRemoteDevice().getAddress());
        mAdapter.cancelDiscovery();
        ConnectedThread thread = new ConnectedThread($socket);
        thread.start();
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket, because mmSocket is final
            BluetoothSocket tmp = null;
//         mmDevice = device;
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }


        public void run() {
            // Cancel discovery because it will slow down the connection
            mAdapter.cancelDiscovery();
            try {
                // Connect the device through the socket. This will block until it succeeds or throws an exception
                mmSocket.connect();
            } catch (Exception e1) {
                Log.e("BTService.java | run", "|==" + "connect fail" + "|");

                e1.printStackTrace();
                // Unable to connect; close the socket and get out
                try {
                    if (mmSocket.isConnected())
                        mmSocket.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                return;
            }
            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }


        */
/**
         * Will cancel an in-progress connection, and close the socket
         *//*

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // Get the input and output streams, using temp objects because member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }



        public void run() {
            //byte[] buffer = new byte[1024]; // buffer store for the stream
            //int bytes; // bytes returned from read()
            byte[] readBuffer = new byte[10];
            int readBufferPosition = 0;
            final byte delimiter = 10;

            while (true) {
                try {
                    // Read from the InputStream
                    //bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI Activity
//               mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    //Log.i("BTService.java | run", "|==" + bytes2String(buffer, bytes) + "|");

                    int bytesAvailable = mmInStream.available();

                    if (bytesAvailable > 0) {
                        final byte[] packetBytes = new byte[bytesAvailable];
                        mmInStream.read(packetBytes);

                        for (int i = 0; i < bytesAvailable; i++) {
                            byte b = packetBytes[i];

                            if (b == delimiter) {
                                if (readBuffer[0] == -1) {
                                    if (readBuffer[1] == 85) {
                                        if (readBuffer[4] == 8) {
                                            brooch_DB = String.valueOf((int) readBuffer[5]);
                                            Intent intent = new Intent("kr.mint.bluetooth.receive");
                                            intent.putExtra("signal", bytes2String(readBuffer[5]));
                                            _context.sendBroadcast(intent);
                                            readBufferPosition = 0;
                                        } else if (readBuffer[4] == 7) {
                                            brooch_safe = String.valueOf((int) readBuffer[5]);
                                            Intent intent = new Intent("kr.mint.bluetooth.receive");
                                            intent.putExtra("signal", "safe mode");
                                            _context.sendBroadcast(intent);
                                            readBufferPosition = 0;
                                            // safe 모드 (safe 모듈 실행)
                                        } else if (readBuffer[4] == 2) {
                                            // 녹음 데이터 min, avg, max
                                            callrecv_min = String.valueOf((int) readBuffer[5]);
                                            callrecv_avg = String.valueOf((int) readBuffer[6]);
                                            callrecv_max = String.valueOf((int) readBuffer[7]);
                                            Intent intent = new Intent("kr.mint.bluetooth.receive");
                                            intent.putExtra("signal", "min, avg, max save");
                                            _context.sendBroadcast(intent);
                                            readBufferPosition = 0;
                                        }
                                    }
                                }

                            } else {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        private String bytes2String(byte b) {
            ArrayList<String> result = new ArrayList<String>();
            //for (int i = 0; i < count; i++) {

            String myInt = String.valueOf((int) b);
//                String myInt =  Integer.toHexString((int) (b[i] & 0xFF));
            result.add(myInt + " dB");

            // }
            return TextUtils.join("-", result);
        }

        */
/* Call this from the main Activity to send data to the remote device *//*

        public void writesSelect(int num) throws IOException {

            byte[] call1 = {-1, 85, 8, 0, 1, 0, 0, 0, 0, 10};
            byte[] callrecv2 = {-1, 85, 8, 0, 2, 0, 0, 0, 0, 10};
            byte[] configure3 = {-1, 85, 8, 0, 2, 60, 3, 3, 3, 10};
            byte[] cation4 = {-1, 85, 8, 0, 4, 0, 0, 0, 0, 10};
            byte[] serious5 = {-1, 85, 8, 0, 5, 0, 0, 0, 0, 10};
            byte[] warning6 = {-1, 85, 8, 0, 6, 0, 0, 0, 0, 10};

            switch (num) {
                case 1:
                    for (int i = 0; i <= 9; i++) {
                        mmOutStream.write(call1[i]);
                    }
                    break;
                case 2:
                    for (int i = 0; i <= 9; i++) {
                        mmOutStream.write(callrecv2[i]);
                    }
                    break;
                case 3:
                    for (int i = 0; i <= 9; i++) {
                        mmOutStream.write(configure3[i]);
                    }
                    break;
                case 4:
                    for (int i = 0; i <= 9; i++) {
                        mmOutStream.write(cation4[i]);
                    }
                    break;
                case 5:
                    for (int i = 0; i <= 9; i++) {
                        mmOutStream.write(serious5[i]);
                    }
                    break;
                case 6:
                    for (int i = 0; i <= 9; i++) {
                        mmOutStream.write(warning6[i]);
                    }
                    break;
            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        */
/* Call this from the main Activity to shutdown the connection *//*

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
}
*/
