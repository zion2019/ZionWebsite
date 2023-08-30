<template>
  <div class="dock-container">
    <div class="dock" v-if="showDock">
      <div class="dock-item-container">
        <div class="dock-item">
          <img src="../assets/home.svg" alt="Home" @click="router.push('/')">
        </div>
        <div class="dock-item">
          <img src="../assets/learning.svg" alt="Learning" @click="router.push('/learning')">
        </div>
      </div>
      
    </div>
    <div class="sidebar-container" >
      <div class="sidebar" @click="toggleDock">
        <!-- Sidebar content -->
        <el-icon v-if="!showDock" size="large"><CaretRight /></el-icon>
        <el-icon v-if="showDock" size="large"><CaretLeft /></el-icon>
      </div>
      
      
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter()

const showDock = ref(false);

function toggleDock() {
  showDock.value = !showDock.value;
}
</script>

<style scoped>
.dock-container {
  position: fixed;
  left: 0;
  z-index: 1;
  display: flex;
}

.hidden {
  visibility: hidden;
}

.sidebar-container {
  z-index: 2;
  cursor: pointer;
  display: flex;
  align-items: center;
}
.sidebar{
  height: 50px;
    background: #ccc;
    /* box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1); */
    border-radius: 0px 5px 5px 0px;
    display: flex;
    align-items: center;
}

.dock { 
  z-index: 2;
  transition: transform 1s ease-in-out;
  display: flex;
  justify-content: center; /* Horizontally center the child div */
  align-items: center; /* Vertically center the child div */
}
.dock-item-container {
  background-color: #ccc;
  display: flex;
  flex-direction: column;
  justify-content: center; 
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}
.dock-item {
  margin-bottom: 10px;
  margin-top: 10px;
  transition: transform 0.3s;
}
.dock-item img {
  width: 50px;
  height: 50px;
  border-radius: 20px;
  cursor: pointer;
}
.dock-item:hover {
  transform: scale(1.1);
}
</style>