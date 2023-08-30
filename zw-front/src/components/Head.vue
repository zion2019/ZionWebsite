vue
<template>
  <div class="head cartoon-style">
    <div class="logo">
      <img src="../assets/logo.svg" alt="Website Icon" />
    </div>
    <div class="navigation">
      <button class="btn" :class="{ 'disabled': !canGoBack }">
        <el-icon><CaretLeft /></el-icon>
      </button>
      <button class="btn" :class="{ 'disabled': !canGoForward }">
        <el-icon><CaretRight /></el-icon>
      </button>
    </div>
    <div class="user">
      <img src="../assets/logo.svg" alt="User Avatar" class="avatar" @mouseover="showDropdown" @mouseleave="hideDropdown" />
      <div class="dropdown" v-show="isDropdownVisible">
        <ul>
          <li>个人信息</li>
          <li>退出登录</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';

export default {
  setup() {
    const canGoBack = ref(true);
    const canGoForward = ref(false);
    const isDropdownVisible = ref(false);

    const showDropdown = () => {
      isDropdownVisible.value = true;
    };

    const hideDropdown = () => {
      isDropdownVisible.value = false;
    };

    return {
      canGoBack,
      canGoForward,
      isDropdownVisible,
      showDropdown,
      hideDropdown
    };
  }
};
</script>

<style scoped>
.head {
display: flex;
align-items: center;
justify-content: space-between;
padding: 20px;
}

.cartoon-style {
    position: absolute;
    top: 0px;
    width: 100%;
    height: 3%;
    background-color: rgba(0, 0, 0, 0.15);
}

.logo img {
  height: 30px;
}

.navigation {
  margin-left: 20px;
}

.btn {
  margin-right: 10px;
  padding: 8px;
  background-color: rgba(0, 0, 0, 0);
  border: none;
  color: #000;
  font-size: 20px;
  cursor: pointer;
}

.disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.user {
  display: flex;
  align-items: center;
  position: relative;
}

.avatar {
  height: 30px;
  border-radius: 50%;
  cursor: pointer;
}

.dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  width: 120px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  padding: 8px;
  display: none;
}

.dropdown ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.dropdown ul li {
  padding: 4px 0;
  cursor: pointer;
}

.dropdown ul li:hover {
  color: #ffcc00;
}
</style>