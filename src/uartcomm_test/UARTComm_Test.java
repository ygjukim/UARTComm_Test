/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uartcomm_test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.Scanner;
import jdk.dio.DeviceManager;
import jdk.dio.uart.UART;
import jdk.dio.uart.UARTConfig;

/**
 *
 * @author yjkim
 */
public class UARTComm_Test {
    
    private UART uart;
    private BufferedReader in;
    private BufferedWriter out;

    public UARTComm_Test(String controllerName) throws IOException {
        if (controllerName == null)
            controllerName = "ttyAMA0";
        
        UARTConfig config;
        config = new UARTConfig( 
            controllerName, 1, 115200,
            UARTConfig.DATABITS_8,
            UARTConfig.PARITY_NONE,
            UARTConfig.STOPBITS_1,
            UARTConfig.FLOWCONTROL_NONE
        );
        
        uart = (UART)DeviceManager.open(config);
        in = new BufferedReader(Channels.newReader(uart, "UTF-8"));
        out = new BufferedWriter(Channels.newWriter(uart, "UTF-8"));
        uart.setReceiveTimeout(100);
    }
    
    public void run() throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.print("input message or 'q'(quit): ");
        byte[] message;
        for (String line = input.nextLine(); 
            !line.equals("q");
            line = input.nextLine()) {
            message = line.getBytes();
            out.write(line);
            out.newLine();
            out.flush();
            System.out.println("Arduino: " + in.readLine());
            System.out.print("input message or 'q'(quit): ");
        }
        
        close();
    }
    
    private void close() throws IOException {
        in.close();
        out.close();
        uart.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        UARTComm_Test echo = new UARTComm_Test(args.length == 1 ? args[0] : null);
        echo.run();
    }
    
}
