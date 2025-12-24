package com.generator.documentation.features.generator.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generator.documentation.features.collector.model.ProjectFile;
import com.generator.documentation.features.collector.service.CollectorOrchestrator;
import com.generator.documentation.features.generator.service.AiService;
import com.generator.documentation.features.generator.service.AiServiceFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentationController.class)
class DocumentationControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private CollectorOrchestrator collectorOrchestrator;
    @MockitoBean private AiServiceFactory aiServiceFactory;
    @MockitoBean private AiService aiService;

    @Test
    void shouldReturnSuccessResponse() throws Exception {
        ReadmeRequest request = new ReadmeRequest("https://github.com/user/repo", null, "gemini");

        // Return a mock file so the controller doesn't trigger the "No files found" error
        ProjectFile mockFile = ProjectFile.builder().path("App.java").content("code").build();

        when(collectorOrchestrator.collect(any(), any())).thenReturn(List.of(mockFile));
        when(aiServiceFactory.getService("gemini")).thenReturn(aiService);
        when(aiService.generate(any())).thenReturn("# README Content");

        mockMvc.perform(post("/api/v1/documentation/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("# README Content"));
    }

    @Test
    void shouldReturn400WhenRequestIsInvalid() throws Exception {
        ReadmeRequest invalidRequest = new ReadmeRequest(null, null, null);

        mockMvc.perform(post("/api/v1/documentation/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}