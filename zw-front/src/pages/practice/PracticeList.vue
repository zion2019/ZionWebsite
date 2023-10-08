<template>
    <window :buttons="buttons">
        <List editRoutor="PointEdit" @keywordChange="keywordChange" @toNew="practise" @back="back">
            <div class="infinite-list-wrapper" style="overflow: auto">
                <ul
                v-infinite-scroll="load"
                class="list"
                :infinite-scroll-disabled="disabled">
                <li 
                    v-for="point in topics" :key="point.id" 
                    @click="practise(point)" class="list-item" :class=point.background>
                        <el-text size="large" tag="i">{{point.fullTitle}}</el-text>
                        <span class="un-parctise-bubble" v-if="point.undoCount>0">{{point.undoCount}}</span>
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
import {PracticeService} from '../../api/api'
import '../../assets/carousel.css'
import { ElMessage } from 'element-plus'

// 路由相关导入
import { useRouter } from 'vue-router';
import {useStore} from 'vuex'
const store = useStore()
const router = useRouter();

interface Topic {
  fullTitle: string;
  id: string;
  topicId:string;
  pointId:string;
  undoCount: number;
  background:string;
  toDayCompletePercent: number;
}

// 按钮定义
const buttons = [
      {
        'type':'warning'
        ,'isPlain':true
        ,'label':'首页'
        ,'function':back
      }
]

const keyword = ref('');
const topics = ref<Topic[]>([]);
const pageNo = ref(0);
const pageSize = 10;
const total = ref<number | null>(1);
const loading = ref(false);
const noMore = computed(() => total.value == null || topics.value.length >= total.value);
const disabled = computed(() => loading.value || noMore.value);
const load = async () => {
  if (loading.value || noMore.value) return;

  loading.value = true;

  try {
    const res = await PracticeService.today({
      pageNo: pageNo.value,
      pageSize,
      title:keyword.value
    }) as any;
    if(res.dataList != null && res.dataList.length > 0){
        topics.value.push(...res.dataList);
    }
    total.value = res.total;
    pageNo.value++;
  } catch (error) {
    total.value = 0;
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
/** 清空数据重新加载 */
function resetList() {
  topics.value = [];
  pageNo.value = 0;
  total.value = 1;
  load();
}

/** 知识点详情页面 */
function practise(topic : Topic){
  if(topic.toDayCompletePercent <= 100 && topic.toDayCompletePercent > 0){
    ElMessage.success("已经复习完啦");
    return;
  }
  store.commit('setTopicId',topic.topicId);
  store.commit('setPointId',topic.pointId);
  store.commit('setFullTitle',topic.fullTitle);
  router.push({ name: 'Practice'});
}

/** 回退至学习页 */
function back(){
  router.push({ name: 'Learning'});
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
  justify-content: space-between;
  height: 50px;
  padding-left: 5px;
  padding-right: 5px; 
  color: var(--el-color-danger);
  border: 1px solid #ddd; /* Add a border */
  border-radius: 10px;
}
.infinite-list-wrapper .list-item + .list-item {
  margin-top: 10px;
}
.un-parctise-bubble {
    position: relative;
    display: inline-block;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background-color: red;
    color: white;
    font-size: 12px;
    text-align: center;
    line-height: 20px;
}
</style>