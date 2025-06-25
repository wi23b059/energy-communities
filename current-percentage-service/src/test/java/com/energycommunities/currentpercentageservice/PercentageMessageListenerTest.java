package com.energycommunities.currentpercentageservice;

import com.energycommunities.currentpercentageservice.messaging.PercentageMessageListener;
import com.energycommunities.currentpercentageservice.model.PercentageData;
import com.energycommunities.currentpercentageservice.model.PercentageMessage;
import com.energycommunities.currentpercentageservice.repository.PercentageDataRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PercentageMessageListenerTest {
    @Test
    void testReceiveSavesPercentageData() {
        // Arrange
        PercentageDataRepository mockRepo = Mockito.mock(PercentageDataRepository.class);
        PercentageMessageListener listener = new PercentageMessageListener(mockRepo);

        LocalDateTime now = LocalDateTime.now();
        PercentageMessage message = new PercentageMessage();
        message.setHour(now);
        message.setCommunityDepleted(25.0);
        message.setGridPortion(75.0);

        // Act
        listener.receive(message);

        // Assert
        ArgumentCaptor<PercentageData> captor = ArgumentCaptor.forClass(PercentageData.class);
        verify(mockRepo, times(1)).save(captor.capture());

        PercentageData saved = captor.getValue();
        assertThat(saved.getHour()).isEqualTo(now);
        assertThat(saved.getCommunityDepleted()).isEqualTo(25.0);
        assertThat(saved.getGridPortion()).isEqualTo(75.0);
    }
}