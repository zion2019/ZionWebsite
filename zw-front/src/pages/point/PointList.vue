<template>
    <window :buttons="buttons">
        <List editRoutor="PointEdit" @keywordChange="keywordChange">
          <div class="infinite-list-wrapper" style="overflow: auto">
            <ul
              v-infinite-scroll="load"
              class="list"
              :infinite-scroll-disabled="disabled">
              <li 
                v-for="point in points" :key="point.id" 
                @click="editPoint(point.id)"
                class="list-item">
                {{ point.title }}
              </li>
            </ul>
            <p v-if="loading">Loading...</p>
            <p v-if="noMore">No more</p>
         </div>
        </List>
    </window>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import Window from '../../components/Window.vue';
import List from '../../components/List.vue';
import { PointService } from '../../api/api';
// 路由相关导入
import { useRouter } from 'vue-router';
import {useStore} from 'vuex'
const store = useStore()
const router = useRouter();
const topicId = store.state.topicId;

interface Point {
  title: string;
  id: string;
}

// 按钮定义
const buttons = [
      {
        'type':'warning'
        ,'isPlain':true
        ,'label':'返回'
        ,'function':back
      },
      {
        'type':'success'
        ,'label':'添加'
        ,'function': newPoint
      }
]

const points = ref<Point[]>([]);
const pageNo = ref(0);
const pageSize = 10;
const total = ref<number | null>(1);
const keyword = ref('');

const loading = ref(false);
const noMore = computed(() => total.value == null || points.value.length >= total.value);
const disabled = computed(() => loading.value || noMore.value);

const load = async () => {
  if (loading.value || noMore.value) return;
  if (!topicId){
    total.value = 0;
  }
  loading.value = true;

  try {
    const res = await PointService.page({
      pageNo: pageNo.value,
      pageSize,
      topicId,
      title:keyword.value
    }) as any;
    if(res.dataList != null && res.dataList.length > 0){
      points.value.push(...res.dataList);
    }
    total.value = res.total;
    pageNo.value++;
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};

onMounted(load);

/** 搜索框监听 */
function keywordChange(val) {
  keyword.value = val;
  console.log(keyword.value);
  resetList();
}
function resetList() {
  points.value = [];
  pageNo.value = 0;
  total.value = 1;
  load();
}

/** 编辑知识点 */
function editPoint(pointId){
  store.commit('setPointId',pointId);
  store.commit('setTopicId',topicId);
  router.push({ name: 'PointEdit'});
}
function newPoint(){
  store.commit('setTopicId',topicId);
  store.commit('setPointId',null);
  router.push({ name: 'PointEdit'});
}

/** 回退至主题页 */
function back(){
  store.commit('setTopicId',topicId);
  router.push({ name: 'TopicEdit'});
}

</script>

<style scoped>
.infinite-list-wrapper {
  max-height: 60vh;
  text-align: center;
  border: 1px solid #ddd; /* Add a border */
  padding: 10px; /* Add padding for spacing */
  border-radius: 10px;
  margin-bottom: 10px;
}
.infinite-list-wrapper .list {
  padding: 0;
  margin: 0;
  list-style: none;
}

.infinite-list-wrapper .list-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  background: var(--el-color-danger-light-9);
  color: var(--el-color-danger);
  border: 1px solid #ddd; /* Add a border */
  border-radius: 10px;
}
.infinite-list-wrapper .list-item + .list-item {
  margin-top: 10px;
}
</style>