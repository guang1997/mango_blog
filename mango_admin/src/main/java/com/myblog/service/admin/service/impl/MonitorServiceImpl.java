package com.myblog.service.admin.service.impl;

import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import com.myblog.service.admin.entity.dto.monitor.*;
import com.myblog.service.admin.service.MonitorService;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.FileUtil;
import com.myblog.service.base.util.IpUtils;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.VirtualMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 监控页面 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-28
 */
@Slf4j
@Service
public class MonitorServiceImpl implements MonitorService {

    private final DecimalFormat df = new DecimalFormat("0.00");

    /**
     * 查询服务器信息
     * @return
     */
    @Override
    public Response getServers(){
        Response response = Response.ok();
        try {
            SystemInfo si = new SystemInfo();
            OperatingSystem os = si.getOperatingSystem();
            HardwareAbstractionLayer hal = si.getHardware();
            // 系统信息
            response.data(Constants.ReplyField.SYS, getSystemInfo(os));
            // cpu 信息
            response.data(Constants.ReplyField.CPU, getCpuInfo(hal.getProcessor()));
            // 内存信息
            response.data(Constants.ReplyField.MEMORY, getMemoryInfo(hal.getMemory()));
            // 交换区信息
            response.data(Constants.ReplyField.SWAP, getSwapInfo(hal.getMemory()));
            // 磁盘
            response.data(Constants.ReplyField.DISK, getDiskInfo(os));
            response.data(Constants.ReplyField.TIME, ThreadSafeDateFormat.format(new Date(), ThreadSafeDateFormat.TIME));
        } catch (Exception e) {
            log.error("getServers failed, exception:", e);
            return Response.setResult(ResultCodeEnum.QUERY_FAILED);
        }
        return response;
    }

    /**
     * 获取磁盘信息
     * @return /
     */
    private DiskDto getDiskInfo(OperatingSystem os) {
        DiskDto diskDto = new DiskDto();
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        String osName = System.getProperty("os.name");
        long available = 0, total = 0;
        for (OSFileStore fs : fsArray){
            // windows 需要将所有磁盘分区累加，linux 和 mac 直接累加会出现磁盘重复的问题，待修复
            if(osName.toLowerCase().startsWith(Constants.SystemConstant.WIN)) {
                available += fs.getUsableSpace();
                total += fs.getTotalSpace();
            } else {
                available = fs.getUsableSpace();
                total = fs.getTotalSpace();
                break;
            }
        }
        long used = total - available;
        diskDto.setTotal(total > 0 ? FileUtil.getSize(total) : "?");
        diskDto.setAvailable(FileUtil.getSize(available));
        diskDto.setUsed(FileUtil.getSize(available));
        if(total != 0){
            diskDto.setUsageRate(df.format(used/(double)total * 100));
        } else {
            diskDto.setUsageRate("0");
        }
        return diskDto;
    }

    /**
     * 获取交换区信息
     * @param memory /
     * @return /
     */
    private SwapDto getSwapInfo(GlobalMemory memory) {
        SwapDto swapDto = new SwapDto();
        VirtualMemory virtualMemory = memory.getVirtualMemory();
        long total = virtualMemory.getSwapTotal();
        long used = virtualMemory.getSwapUsed();
        swapDto.setTotal(FormatUtil.formatBytes(total));
        swapDto.setUsed(FormatUtil.formatBytes(used));
        swapDto.setAvailable(FormatUtil.formatBytes(total - used));
        if(used == 0){
            swapDto.setUsageRate("0");
        } else {
            swapDto.setUsageRate(df.format(used/(double)total * 100));
        }
        return swapDto;
    }

    /**
     * 获取内存信息
     * @param memory /
     * @return /
     */
    private MemoryDto getMemoryInfo(GlobalMemory memory) {
        MemoryDto memoryDto = new MemoryDto();
        memoryDto.setTotal(FormatUtil.formatBytes(memory.getTotal()));
        memoryDto.setAvailable(FormatUtil.formatBytes(memory.getAvailable()));
        memoryDto.setUsed(FormatUtil.formatBytes(memory.getTotal() - memory.getAvailable()));
        memoryDto.setUsageRate(df.format((memory.getTotal() - memory.getAvailable())/(double)memory.getTotal() * 100));
        return memoryDto;
    }

    /**
     * 获取Cpu相关信息
     * @param processor /
     * @return /
     */
    private CpuDto getCpuInfo(CentralProcessor processor) {
        CpuDto cpuDto = new CpuDto();
        cpuDto.setName(processor.getProcessorIdentifier().getName());
        cpuDto.setCpuNum(processor.getPhysicalPackageCount() + "个物理CPU");
        cpuDto.setCore(processor.getPhysicalProcessorCount() + "个物理核心");
        cpuDto.setCoreNumber(processor.getPhysicalProcessorCount());
        cpuDto.setLogic(processor.getLogicalProcessorCount() + "个逻辑CPU");
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 等待1秒...
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;
        cpuDto.setUsed(df.format(100d * user / totalCpu + 100d * sys / totalCpu));
        cpuDto.setIdle(df.format(100d * idle / totalCpu));
        return cpuDto;
    }

    /**
     * 获取系统相关信息,系统、运行天数、系统IP
     * @param os /
     * @return /
     */
    private SysDto getSystemInfo(OperatingSystem os){
        SysDto sysDto = new SysDto();
        // jvm 运行时间
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        Date date = new Date(time);
        // 计算项目运行时间
        String formatBetween = DateUtil.formatBetween(date, new Date(), BetweenFormater.Level.HOUR);
        // 系统信息
        sysDto.setSystemInfo(os.toString());
        sysDto.setDay(formatBetween);
        sysDto.setIp(IpUtils.getLocalIp());
        return sysDto;
    }
}
