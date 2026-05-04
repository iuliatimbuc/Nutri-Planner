import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

class NotificationService {
    constructor() {
        this.client = null;
    }

    connect(userId, onNotification) {
        this.client = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
            onConnect: () => {
                this.client.subscribe(`/topic/meal-notification/${userId}`, (message) => {
                    onNotification(message.body);
                });
            }
        });
        this.client.activate();
    }

    disconnect() {
        if (this.client) {
            this.client.deactivate();
        }
    }
}
export default new NotificationService();