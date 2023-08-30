<template>
  <div :style="windowStyles" class="window" v-show="!isClose">
    <div class="window-top-bar">
      <div class="window-top-buttons">
        <button class="window-top-button close" @click="closeWindow" @mouseover="hoverButton('close')" @mouseleave="resetButton('close')">
          <el-icon><CloseBold /></el-icon>
        </button>
        <button class="window-top-button minimize" @click="minimizeWindow" @mouseover="hoverButton('minimize')" @mouseleave="resetButton('minimize')">
          <el-icon><SemiSelect /></el-icon>
        </button>
        <button class="window-top-button maximize" @click="maximizeWindow" @mouseover="hoverButton('maximize')" @mouseleave="resetButton('maximize')">
          <el-icon><Plus /></el-icon>
        </button>
      </div>
    </div>
    <div class="window-content-container">
      <div class="window-content">
        <slot></slot>
      </div>
      <div class="window-option-container">
        <el-button v-for="button in (buttons as Button[])" 
          :type="button.type" 
          :plain="button.isPlain ? true : false"
          @click="button.function">
          {{ button.label }}
        </el-button>
    </div>

    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';

interface Button{
  type:String;
  isPlain:boolean;
  function:Function;
  label:String;
}

const props = defineProps({
  windowSize: {
    type: Number,
    default: 1,
  },
  buttons: {
    type: Array,
    default: () => []
  }
});

const isClose = ref(false);
const windowStyles = ref({
  width: `${props.windowSize * 100}%`,
  height: `${props.windowSize * 100}%`,
});

const closeWindow = () => {
  isClose.value = true;
};

const minimizeWindow = () => {
  windowStyles.value = {
    width: '80%',
    height: '80%',
  };
};

const maximizeWindow = () => {
  windowStyles.value = {
    width: '100%',
    height: '100%',
  };
};

const hoverButton = (button: string) => {
  const buttonElement = document.querySelector(`.${button}`);
  buttonElement?.classList.add('hover');
};

const resetButton = (button: string) => {
  const buttonElement = document.querySelector(`.${button}`);
  buttonElement?.classList.remove('hover');
};
</script>

<style scoped>
.window-option-container {
  display: flex;
  justify-content: center;
}

.window-content-container {
  display: flex;
  flex-direction: column;
  /* justify-content: space-between; */
  height: 95%;
}
.window-content{
  max-height: 95%;
  overflow-y: auto;
}

.window {
  margin: auto;
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  border-radius: 5px;
  overflow: hidden;
  position: relative;
}

.window-top-bar {
  height: 30px;
  background-color: #ccc;
  display: flex;
  align-items: center;
  padding: 0 10px;
}

.title {
  flex-grow: 1;
  font-weight: bold;
  color: #333;
}

.window-top-buttons {
  display: flex;
  gap: 5px;
}

.window-top-button {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  border: none;
  outline: none;
  cursor: pointer;
  transition: transform 0.2s ease-in-out;
  display: flex;
  justify-content: center;
  align-items: center;
}

.window-top-button.hover {
  transform: scale(1.1);
}

.window-top-button.close {
  background-color: #ff5f57;
}

.window-top-button.minimize {
  background-color: #ffbd2e;
}

.window-top-button.maximize {
  background-color: #28c940;
}

.icon-close::before {
  content: "x";
}

.icon-minimize::before {
  content: "-";
}

.icon-maximize::before {
  content: "+";
}
</style>