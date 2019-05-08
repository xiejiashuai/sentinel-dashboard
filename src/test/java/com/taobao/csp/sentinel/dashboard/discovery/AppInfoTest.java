package com.taobao.csp.sentinel.dashboard.discovery;

import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.*;

public class AppInfoTest {
    @Test
    public void testConcurrentGetMachines() throws Exception {
        AppInfo appInfo = new AppInfo("testApp");
        appInfo.addMachine(genMachineInfo("hostName1", "10.18.129.91"));
        appInfo.addMachine(genMachineInfo("hostName2", "10.18.129.92"));
        Set<MachineInfo> machines = appInfo.getMachines();
        new Thread(() -> {
            try {
                for (MachineInfo m : machines) {
                    System.out.println(m);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                    }
                }
            } catch (ConcurrentModificationException e) {
                e.printStackTrace();
                assertTrue(false);
            }

        }).start();
        Thread.sleep(100);
        try {
            appInfo.addMachine(genMachineInfo("hostName3", "10.18.129.93"));
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        Thread.sleep(1000);
    }

    private MachineInfo genMachineInfo(String hostName, String ip) {
        MachineInfo machine = new MachineInfo();
        machine.setApp("testApp");
        machine.setHostname(hostName);
        machine.setIp(ip);
        machine.setPort(8719);
        machine.setVersion(String.valueOf(System.currentTimeMillis()));
        return machine;
    }

    @Test
    public void addRemoveMachineTest() {
        AppInfo appInfo = new AppInfo("default");
        assertEquals("default", appInfo.getApp());
        assertEquals(0, appInfo.getMachines().size());
        //add one
        {
            MachineInfo machineInfo = new MachineInfo();
            machineInfo.setApp("default");
            machineInfo.setHostname("bogon");
            machineInfo.setIp("127.0.0.1");
            machineInfo.setPort(3389);
            machineInfo.setTimestamp(new Date());
            machineInfo.setVersion("0.4.1");
            appInfo.addMachine(machineInfo);
        }
        assertEquals(1, appInfo.getMachines().size());
        //add duplicated one
        {
            MachineInfo machineInfo = new MachineInfo();
            machineInfo.setApp("default");
            machineInfo.setHostname("bogon");
            machineInfo.setIp("127.0.0.1");
            machineInfo.setPort(3389);
            machineInfo.setTimestamp(new Date());
            machineInfo.setVersion("0.4.2");
            appInfo.addMachine(machineInfo);
        }
        assertEquals(1, appInfo.getMachines().size());
        //add different one
        {
            MachineInfo machineInfo = new MachineInfo();
            machineInfo.setApp("default");
            machineInfo.setHostname("bogon");
            machineInfo.setIp("127.0.0.1");
            machineInfo.setPort(3390);
            machineInfo.setTimestamp(new Date());
            machineInfo.setVersion("0.4.3");
            appInfo.addMachine(machineInfo);
        }
        assertEquals(2, appInfo.getMachines().size());
        appInfo.removeMachine("127.0.0.1", 3389);
        assertEquals(1, appInfo.getMachines().size());
        appInfo.removeMachine("127.0.0.1", 3390);
        assertEquals(0, appInfo.getMachines().size());
    }

}