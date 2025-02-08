package com.gotcha.earlytable;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

@Component
public class FCMInitializer {

    @PostConstruct
    public void initialize() {
        try {
            String firebaseConfigPath = System.getenv("FIREBASE_CONFIG_PATH");
            if (firebaseConfigPath == null || firebaseConfigPath.isEmpty()) {
                throw new RuntimeException("FIREBASE_CONFIG_PATH 환경 변수가 설정되지 않았습니다.");
            }

            FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();


            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            System.out.println("🔥 Firebase가 성공적으로 초기화되었습니다.");
        } catch (IOException e) {
            throw new RuntimeException("Firebase 설정 파일을 로드하는 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
