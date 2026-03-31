package com.example.meet.controller;

import com.example.meet.common.Result;
import com.example.meet.entity.MeetingMinute;
import com.example.meet.service.MeetingMinuteService;
import com.example.meet.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/minutes")
@CrossOrigin(origins = "*")
public class MeetingMinuteController {
    
    @Autowired
    private MeetingMinuteService meetingMinuteService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping
    public Result<List<MeetingMinute>> getList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String creatorId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            List<MeetingMinute> list = meetingMinuteService.search(keyword, status, creatorId, startDate, endDate);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public Result<MeetingMinute> getById(@PathVariable String id) {
        try {
            MeetingMinute meetingMinute = meetingMinuteService.getById(id);
            if (meetingMinute == null) {
                return Result.error("会议纪要不存在");
            }
            return Result.success(meetingMinute);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping
    public Result<MeetingMinute> create(@RequestHeader("Authorization") String token,
                                       @Valid @RequestBody MeetingMinute meetingMinute) {
        try {
            String userId = getUserIdFromToken(token);
            meetingMinute.setCreatorId(userId);
            meetingMinute.setHostId(userId);
            MeetingMinute created = meetingMinuteService.create(meetingMinute);
            return Result.success("创建成功", created);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public Result<Void> update(@RequestHeader("Authorization") String token,
                                   @PathVariable String id,
                                   @RequestBody MeetingMinute meetingMinute) {
        try {
            String userId = getUserIdFromToken(token);
            MeetingMinute existing = meetingMinuteService.getById(id);
            if (existing == null) {
                return Result.error("会议纪要不存在");
            }
            if (!existing.getCreatorId().equals(userId)) {
                return Result.error("无权修改此会议纪要");
            }
            meetingMinute.setId(id);
            meetingMinute.setCreatorId(existing.getCreatorId());
            meetingMinute.setHostId(existing.getHostId());
            meetingMinuteService.update(meetingMinute);
            return Result.success("更新成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@RequestHeader("Authorization") String token,
                                     @PathVariable String id,
                                     @RequestBody Map<String, String> request) {
        try {
            String userId = getUserIdFromToken(token);
            MeetingMinute meetingMinute = meetingMinuteService.getById(id);
            if (meetingMinute == null) {
                return Result.error("会议纪要不存在");
            }
            if (!meetingMinute.getCreatorId().equals(userId)) {
                return Result.error("无权修改此会议纪要");
            }
            String status = request.get("status");
            if (status == null || status.isEmpty()) {
                return Result.error("状态不能为空");
            }
            meetingMinuteService.updateStatus(id, status);
            return Result.success("状态更新成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@RequestHeader("Authorization") String token,
                                   @PathVariable String id) {
        try {
            String userId = getUserIdFromToken(token);
            MeetingMinute meetingMinute = meetingMinuteService.getById(id);
            if (meetingMinute == null) {
                return Result.error("会议纪要不存在");
            }
            if (!meetingMinute.getCreatorId().equals(userId)) {
                return Result.error("无权删除此会议纪要");
            }
            meetingMinuteService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    private String getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.getUserIdFromToken(token);
    }
}
