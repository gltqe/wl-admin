package com.gltqe.wladmin.monitor.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.commons.exception.WlException;
import com.gltqe.wladmin.commons.utils.StorageUnitUtil;
import com.gltqe.wladmin.monitor.entity.vo.CpuInfoVo;
import com.gltqe.wladmin.monitor.entity.vo.DiscInfoVo;
import com.gltqe.wladmin.monitor.entity.vo.MemoryInfoVo;
import com.gltqe.wladmin.monitor.entity.vo.ServerInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 系统服务监控
 *
 * @author gltqe
 * @date 2022/7/3 0:49
 **/
@Slf4j
@RestController
@RequestMapping("/monitor")
public class MonitorController {
    /**
     * 获取服务信息
     *
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 0:50
     **/
    @RequestMapping("/getServerInfo")
    public Result getServerInfo() {
        InetAddress localhost = null;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.error("获取服务器信息异常");
        }
        // 服务器名称
        String serverName = localhost.getHostName();
        // 服务器地址
        String serverIp = localhost.getHostAddress();
        // 操作系统名称
        String osName = System.getProperty("os.name");
        // 操作系统架构
        String osArch = System.getProperty("os.arch");
        // java版本
        String javaVersion = System.getProperty("java.version");
        // jvm名称
        String jvmName = System.getProperty("java.vm.name");
        // jvm版本
        String jvmVersion = System.getProperty("java.vm.version");
        // 项目路径
        String projectDir = System.getProperty("user.dir");
        // 运行参数
        String runParameters = ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
        ServerInfoVo serverInfo = new ServerInfoVo();
        serverInfo.setServerName(serverName);
        serverInfo.setServerIp(serverIp);
        serverInfo.setOsName(osName);
        serverInfo.setOsArch(osArch);
        serverInfo.setJavaVersion(javaVersion);
        serverInfo.setJvmName(jvmName);
        serverInfo.setJvmVersion(jvmVersion);
        serverInfo.setProjectDir(projectDir);
        serverInfo.setRunParameters(runParameters);
        return Result.ok(serverInfo);
    }

    /**
     * 获取CPU信息
     *
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 0:50
     **/
    @RequestMapping("/getCpuInfo")
    public Result getCpuInfo() {
        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        ThreadUtil.sleep(1000L);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softIrq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + ioWait + irq + softIrq + steal;
        if (totalCpu <= 0) {
            throw new WlException("获取cpu信息异常");
        }
        BigDecimal decimal100 = new BigDecimal("100");
        BigDecimal totalCpuDecimal = new BigDecimal(totalCpu);
        // cpu总数
        int logicalProcessorCount = processor.getLogicalProcessorCount();
        // 系统使用率
        String sysRate = new BigDecimal(cSys).multiply(decimal100).divide(totalCpuDecimal, 2, RoundingMode.HALF_UP).toPlainString();
        // 用户使用率
        String userRate = new BigDecimal(user).multiply(decimal100).divide(totalCpuDecimal, 2, RoundingMode.HALF_UP).toPlainString();
        // 当前空闲率
        String idleRate = new BigDecimal(idle).multiply(decimal100).divide(totalCpuDecimal, 2, RoundingMode.HALF_UP).toPlainString();
        // 当前使用率
        String usedRate = new BigDecimal("100").subtract(new BigDecimal(idleRate)).toPlainString();
        CpuInfoVo cpuInfo = new CpuInfoVo();
        cpuInfo.setTotal(String.valueOf(logicalProcessorCount));
        cpuInfo.setSysRate(sysRate);
        cpuInfo.setUserRate(userRate);
        cpuInfo.setUsedRate(usedRate);
        cpuInfo.setIdleRate(idleRate);
        return Result.ok(cpuInfo);
    }

    /**
     * 获取内存信息
     *
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 0:50
     **/
    @RequestMapping("/getMemoryInfo")
    public Result getMemoryInfo() {
        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        long totalByte = memory.getTotal();
        long availableByte = memory.getAvailable();
        long usedByte = totalByte - availableByte;
        BigDecimal total = StorageUnitUtil.convert(new BigDecimal(totalByte), StorageUnitUtil.BYTE, StorageUnitUtil.GB, 2);
        BigDecimal used = StorageUnitUtil.convert(new BigDecimal(usedByte), StorageUnitUtil.BYTE, StorageUnitUtil.GB, 2);
        BigDecimal available = StorageUnitUtil.convert(new BigDecimal(availableByte), StorageUnitUtil.BYTE, StorageUnitUtil.GB, 2);
        BigDecimal rate = used.multiply(new BigDecimal(100)).divide(total, 2, BigDecimal.ROUND_HALF_UP);
        MemoryInfoVo memoryInfo = new MemoryInfoVo();
        memoryInfo.setTotal(total.toPlainString());
        memoryInfo.setUsed(used.toPlainString());
        memoryInfo.setFree(available.toPlainString());
        memoryInfo.setRate(rate.toPlainString());
        return Result.ok(memoryInfo);
    }

    /**
     * 获取硬盘信息
     *
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 0:50
     **/
    @RequestMapping("/getDiscInfo")
    public Result getDiscInfo() {
        String userDir = System.getProperty("user.dir");
        File file = new File(userDir);
        long freeSpace = file.getFreeSpace();
        long totalSpace = file.getTotalSpace();
        long usedSpace = totalSpace - freeSpace;
        BigDecimal rate = new BigDecimal(usedSpace).multiply(new BigDecimal(100)).divide(new BigDecimal(totalSpace), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal free = StorageUnitUtil.convert(new BigDecimal(freeSpace), StorageUnitUtil.BYTE, StorageUnitUtil.GB, 2);
        BigDecimal total = StorageUnitUtil.convert(new BigDecimal(totalSpace), StorageUnitUtil.BYTE, StorageUnitUtil.GB, 2);
        BigDecimal used = StorageUnitUtil.convert(new BigDecimal(usedSpace), StorageUnitUtil.BYTE, StorageUnitUtil.GB, 2);
        DiscInfoVo discInfo = new DiscInfoVo();
        discInfo.setFree(free.toPlainString());
        discInfo.setUsed(used.toPlainString());
        discInfo.setTotal(total.toPlainString());
        discInfo.setRate(rate.toPlainString());
        return Result.ok(discInfo);
    }
}
