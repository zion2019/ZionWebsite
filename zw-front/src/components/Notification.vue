vue
<template>
  <div class="notification-container">
    <transition-group name="notification-slide" mode="out-in">
      <div v-for="notification in notifications" :key="notification.id" class="notification" :class="notification.type">
        {{ notification.message }}
      </div>
    </transition-group>
  </div>
</template>

<script>
import { ref } from 'vue';

export default {
  name: 'Notification',
  data() {
    return {
      notifications: []
    };
  },
  methods: {
    success(message) {
      this.showNotification(message, 'success');
    },
    info(message) {
      this.showNotification(message, 'info');
    },
    warning(message) {
      this.showNotification(message, 'warning');
    },
    showNotification(message, type) {
      const notification = {
        id: Date.now(),
        message,
        type
      };
      this.notifications.push(notification);
      setTimeout(() => {
        this.notifications = this.notifications.filter(n => n.id !== notification.id);
      }, 1000);
    }
  }
};
</script>

<style scoped>
.notification-container {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 9999;
}

.notification {
  position: relative;
  padding: 10px 20px;
  margin-bottom: 10px;
  border-radius: 4px;
  color: #fff;
  font-size: 14px;
  font-weight: bold;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.notification-slide-enter-active,
.notification-slide-leave-active {
  transition: all 0.3s;
}

.notification-slide-enter,
.notification-slide-leave-to {
  opacity: 0;
  transform: translateX(100%);
}
</style>