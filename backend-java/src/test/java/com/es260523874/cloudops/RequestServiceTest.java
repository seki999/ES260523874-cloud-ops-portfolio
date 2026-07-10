package com.es260523874.cloudops;

import com.es260523874.cloudops.repository.RequestRepository;
import com.es260523874.cloudops.service.RequestService;

/** 外部ライブラリなしで実行できる最小テストです。 */
public class RequestServiceTest {
    public static void main(String[] args) {
        RequestService service = new RequestService(new RequestRepository());
        if (service.listRequests().isEmpty()) {
            throw new IllegalStateException("初期データが存在しません");
        }
        var created = service.createRequest("verification-project", "Lambda", "dev", "tester");
        if (service.getRequest(created.requestId).isEmpty()) {
            throw new IllegalStateException("作成した申請を取得できません");
        }
        if (service.updateStatus(created.requestId, "APPROVED").isEmpty()) {
            throw new IllegalStateException("ステータス更新に失敗しました");
        }
        System.out.println("RequestServiceTest passed");
    }
}

