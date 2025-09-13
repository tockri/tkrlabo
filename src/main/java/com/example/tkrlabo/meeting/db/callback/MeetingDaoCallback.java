package com.example.tkrlabo.meeting.db.callback;

import org.springframework.data.relational.core.mapping.event.AfterConvertCallback;
import org.springframework.stereotype.Component;

import com.example.tkrlabo.meeting.db.MeetingDao;

@Component
public class MeetingDaoCallback implements AfterConvertCallback<MeetingDao> {

    @Override
    public MeetingDao onAfterConvert(MeetingDao entity) {
        if (entity.getSubject() != null) {
            entity.setSubject(entity.getSubject().toLowerCase());
        }
        return entity;
    }
}
