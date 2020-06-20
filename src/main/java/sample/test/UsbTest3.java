package sample.test;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.usb.*;

/**
 * description: <一句话功能简述>
 *
 * @author Chenz
 * @date 2020/6/16
 */
public class UsbTest3 {

    public static void main(String[] args) throws UsbException {
        UsbServices usbServices = UsbHostManager.getUsbServices();
        processDevice(usbServices.getRootUsbHub());
    }

    private static void processDevice(UsbDevice device) {

        if (device.isUsbHub()) {
            UsbHub hub = (UsbHub) device;
            for (UsbDevice child : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
                processDevice(child);
            }
        } else {
            try {
                dumpName(device);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    private static void dumpName(UsbDevice device) throws UnsupportedEncodingException, UsbException {
        UsbDeviceDescriptor descriptor = device.getUsbDeviceDescriptor();
        byte iManufacturer = descriptor.iManufacturer();
        byte iProduct = descriptor.iProduct();
        short idVendor = descriptor.idVendor();
        short idProduct = descriptor.idProduct();
        byte iSerialNumber = descriptor.iSerialNumber();
        if (iManufacturer == 0 || iProduct == 0) return;
        System.out.println(device.getString(iManufacturer) + "---" + device.getString(iProduct) + "---" + device.getString(iSerialNumber));
        System.out.println(idVendor +" : "+idProduct);
    }

}
