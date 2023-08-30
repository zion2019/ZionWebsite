<template>
    <div class="list-container">
      <div class="list-search">
        <el-input
            v-model="keyword"
            class="w-50 m-1"
            placeholder="输入你要查找的～"
          />
          <el-button :icon="Search" @click="search" type="success" />
      </div>
  
      <slot></slot>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref } from 'vue';
  
  import { Search } from '@element-plus/icons-vue';
  const emits = defineEmits(['keywordChange']);
  const keyword = ref('');


  const search = () => {
    emits('keywordChange', keyword.value);
  }
  </script>
  
  <style scoped>
.list-search {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border: 1px solid #ddd; /* Add a border */
  padding: 10px; /* Add padding for spacing */
  border-radius: 10px;
}
.list-search el-input {
  flex-grow: 1;
  margin-right: 10px;
}
.list {
  padding: 0;
  margin: 0;
  list-style: none;
}

.other-option{
    display: flex;
    justify-content: center;
}


.infinite-list-wrapper {
    max-height: calc(100vh - 220px); /* Adjust as needed */
    overflow: auto;
}

.list {
  padding: 0;
  margin: 0;
  list-style: none;
}



.list-item-content {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.list-item-title {
  font-size: 16px;
  font-weight: bold;
}

.list-item-icons {
  display: flex;
  gap: 10px;
}

.search-icon,
.view-icon,
.delete-icon {
  color: #ff9900;
}

.loading-text {
  font-size: 18px;
  font-weight: bold;
  color: #ff9900;
  margin-top: 10px;
}

.no-more-text {
  font-size: 18px;
  font-weight: bold;
  color: #999;
  margin-top: 10px;
}
  </style>
  