<template>
  <Window>
    <div class="progress-container">
        <div class="card" :class="{'release': isClear, 'practise': !isClear}">
          <div class="card-content">
              <h2 v-if="!isClear">今日份复习～</h2>
              <h2 v-if="isClear">今天没任务啦！</h2>
              <div class="progress" >
                <el-carousel :interval="4000" type="card" height="200px">
                  <el-carousel-item v-for="item in topics" :key="item"  >
                    <div class="progress-item" :class=item.background>
                      <el-progress
                        v-if="item.id != '-1'"
                        @click="practise(item)"
                        style="display: flex;justify-content: center;" 
                        type="circle" 
                        :percentage="item.toDayCompletePercent"
                        :status="progressStatus(item.toDayCompletePercent)" >
                        <el-text tag="b" size="large">{{item.title}}</el-text>
                      </el-progress>
                      <div style="display: flex;justify-content: center;"  v-if="item.id == '-1'">
                        <el-button  type="success" @click="toMore" round>MORE</el-button>
                      </div>
                      
                    </div>
                  </el-carousel-item>
                </el-carousel>
              </div>
          </div>
        </div>
        <div class="card manager">
          <div class="card-content">
              <h2>涨姿势～</h2>
              <div class="quick-option">
                <button class="quick-button" style="background-color: #4169E1;" @click="quick('TopicEdit')">
                  <el-text style="color: #fff;font-weight: bold;"><el-icon ><CirclePlus /></el-icon>创建主题</el-text>
                </button>
                <button class="quick-button" style="background-color: #E0115F;" @click="quick('PointEdit')">
                  <el-text style="color: #fff;font-weight: bold;"><el-icon ><CirclePlus /></el-icon>快捷记录</el-text>
                </button>
                <button class="quick-button" style="background-color: #FFD700;" @click="quick('TopicList')">
                  <el-text style="font-weight: bold;"><el-icon ><CirclePlus /></el-icon>主题管理</el-text>
                </button>
              </div>
          </div>
        </div>
    </div>
  </Window>
  
</template>

<script setup>
  import { onMounted,ref } from 'vue';
  import Window from '../components/Window.vue'
  import { useRouter } from 'vue-router';
  import {PracticeService} from '../api/api';
  import '../assets/carousel.css'
  import { ElMessage } from 'element-plus'
  import {useStore} from 'vuex'
  const store = useStore();

  const isClear = ref(true);
  const topics = ref([]);
  const router = useRouter();
  onMounted(async () => {
    try{
      PracticeService.today({pageNo:0,pageSize:2})
      .then((res)=>{
        topics.value = res.dataList || [];
        if(topics.value.length > 0){
          
          topics.value = dataTemplates.value.map((template, index) => {
            return topics.value[index] || template;
          })
          topics.value.push(dataMore.value);
          isClear.value = false;
        }
        
      }).catch(err=>{
      })
    }catch(err){
      console.log(err);
    }finally{      
    }
    
  })

  /** practise(item.completePercent) */
  function practise(topic){
    if(topic.toDayCompletePercent >= 100){
      ElMessage.success("已经复习完啦");
      return;
    }

    store.commit('setFullTitle',topic.fullTitle);
    store.commit('setTopicId',topic.topicId);
    router.push({name:'Practice'});
  }

  /** 练习列表 */
  function toMore(){
    router.push({name:'PracticeList'});
  }

  // 轮播图进度条样式
  const progressStatus = (completePercent) =>{
    if(completePercent > 30 && completePercent <= 80  ){
        return 'warning';
    }
    if(completePercent > 80){
        return 'success';
    }

    return 'exception';
  }

  /** 快捷创建列表 */
  const quick = (component) =>{
    store.commit('setTopicId',null);
    store.commit('setPointId',null);
    router.push({name:component});
  }

  /**模版数据 */
  const dataTemplates = ref([
    {
        id:'1'
        ,title:'模版主题'
        ,background: 'gloomy'
        ,completePercent: 25.03

    },{
      id:'2'
      ,title:'模版主题'
      ,background: 'shine'
      ,completePercent: 50.05
    }
  ])

  /** 更多项 */
  const dataMore = ref(
    {
      id:'-1'
      ,title:'更多'
      ,background: 'frog'
      ,percentOfMastery: 99
    }
  )

</script>


<style scoped>
.quick_text {
  font-size: 16px;
  font-weight: bold;
  color: #ffffff;
}
.quick-option {
  display: flex;
  flex-direction: column;
}
.quick-button {
  display: inline-block;
  padding: 10px 20px;
  text-align: center;
  text-decoration: none;
  margin-top: 5px;
  border: none;
  border-radius: 10px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.quick-button:hover {
  background-color: #ff9900;
  transform: scale(1.05);
}

.progress {
  height: 30%;
}

.el-carousel__item {
  border-radius: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
  }

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}

.progress-container {
    position: absolute;
    width: 100%;
    height: 100%;
    justify-content: center;
    align-items: center;     /* 垂直居中 */
    display: flex;
}
.card {
  border-radius: 20px;
  justify-content: center;
  background-color: #f0f0f0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  margin-right: 2%;
  overflow: hidden;
  width: 40%;
  height: 80%;
}
.card.practise {
  background: url("../assets/imgs/Strive.svg");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}
.card.release {
  background: url("../assets/imgs/release.svg");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.card.manager {
  background: url("../assets/imgs/Flying.svg");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.card-image {
  height: 50%;
  overflow: hidden;
  border-radius: 20px 20px 0 0;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-content {
  padding: 20px;
}

.card-content h2 {
  font-size: 20px;
  margin-bottom: 10px;
  /* color: #fff;
  mix-blend-mode: difference; */
}

.card-content p {
  font-size: 14px;
  color: #555;
}

@media (max-width: 768px) {
  .progress-container {
    flex-direction: column; /* 纵向排列 */
    justify-content: unset;
  }
  .card {
    margin-top: 10%;
    width: 80%;
    height: 40%;
  }
}
.el-row {
    margin-bottom: 20px;
}
.el-col {
  border-radius: 4px;
}
.bg-purple-new {
  background-color: #e24487;
}
.bg-purple-blue {
  background: #02448f;
}
.bg-purple-black {
  background: #00040c;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
}
.grid-content img {
  width: 10%;
  height: 90%;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}

/* 轮播图背景样式 start */
.progress-item {
  display: flex;
  flex-direction: column;
  display: flex;
  height: 100%;
  width: 100%;
  justify-content: center;
}
/* 轮播图背景样式 end */
.progress .el-carousel .el-carousel__indicators--horizontal{
  display: none;
}
</style>
