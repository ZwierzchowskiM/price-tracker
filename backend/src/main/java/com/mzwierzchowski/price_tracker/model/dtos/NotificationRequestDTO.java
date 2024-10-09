package com.mzwierzchowski.price_tracker.model.dtos;

import com.mzwierzchowski.price_tracker.model.NotificationType;
import lombok.Data;

@Data
public class NotificationRequestDTO {
  private NotificationType notificationType;
  private Double notificationPrice;
    }
