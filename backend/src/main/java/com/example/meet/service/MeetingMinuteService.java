package com.example.meet.service;

import com.example.meet.entity.MeetingMinute;
import com.example.meet.mapper.MeetingMinuteMapper;
import com.example.meet.mapper.MeetingAttendeeMapper;
import com.example.meet.mapper.MinuteCommentMapper;
import com.example.meet.mapper.MinuteAttachmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MeetingMinuteService {
    
    @Autowired
    private MeetingMinuteMapper meetingMinuteMapper;
    
    @Autowired
    private MeetingAttendeeMapper meetingAttendeeMapper;
    
    @Autowired
    private MinuteCommentMapper minuteCommentMapper;
    
    @Autowired
    private MinuteAttachmentMapper minuteAttachmentMapper;
    
    public MeetingMinute getById(String id) {
        return meetingMinuteMapper.selectById(id);
    }
    
    public List<MeetingMinute> getByCreatorId(String creatorId) {
        return meetingMinuteMapper.selectByCreatorId(creatorId);
    }
    
    public List<MeetingMinute> getByHostId(String hostId) {
        return meetingMinuteMapper.selectByHostId(hostId);
    }
    
    public List<MeetingMinute> getByStatus(String status) {
        return meetingMinuteMapper.selectByStatus(status);
    }
    
    public List<MeetingMinute> getAll() {
        return meetingMinuteMapper.selectAll();
    }
    
    public List<MeetingMinute> search(String keyword, String status, String creatorId, String startDate, String endDate) {
        return meetingMinuteMapper.search(keyword, status, creatorId, startDate, endDate);
    }
    
    @Transactional
    public MeetingMinute create(MeetingMinute meetingMinute) {
        if (meetingMinute.getId() == null || meetingMinute.getId().isEmpty()) {
            meetingMinute.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        if (meetingMinute.getCurrentVersion() == null) {
            meetingMinute.setCurrentVersion("V1.0");
        }
        if (meetingMinute.getStatus() == null) {
            meetingMinute.setStatus("draft");
        }
        meetingMinute.setCreatedTime(LocalDateTime.now());
        meetingMinute.setUpdatedTime(LocalDateTime.now());
        meetingMinuteMapper.insert(meetingMinute);
        return meetingMinute;
    }
    
    @Transactional
    public void update(MeetingMinute meetingMinute) {
        if (meetingMinute.getId() == null || meetingMinute.getId().isEmpty()) {
            throw new RuntimeException("会议纪要ID不能为空");
        }
        MeetingMinute existing = meetingMinuteMapper.selectById(meetingMinute.getId());
        if (existing == null) {
            throw new RuntimeException("会议纪要不存在");
        }
        meetingMinute.setUpdatedTime(LocalDateTime.now());
        meetingMinuteMapper.update(meetingMinute);
    }
    
    @Transactional
    public void updateStatus(String id, String status) {
        MeetingMinute meetingMinute = meetingMinuteMapper.selectById(id);
        if (meetingMinute == null) {
            throw new RuntimeException("会议纪要不存在");
        }
        meetingMinuteMapper.updateStatus(id, status);
    }
    
    @Transactional
    public void delete(String id) {
        MeetingMinute meetingMinute = meetingMinuteMapper.selectById(id);
        if (meetingMinute == null) {
            throw new RuntimeException("会议纪要不存在");
        }
        meetingAttendeeMapper.deleteByMinuteId(id);
        minuteCommentMapper.deleteByMinuteId(id);
        minuteAttachmentMapper.deleteByMinuteId(id);
        meetingMinuteMapper.deleteById(id);
    }
}
