<template>
    <Window>
        <div class="practice-container">
            <!-- 基本信息 -->
            <div class="form-wrapper">
                <div class="from-label">基本信息:</div>
                <el-form-item label="题目内容:">
                    <el-text tag="b">{{practice.point.title}}</el-text>
                </el-form-item>
                <el-form-item label="所属主题:">
                    <el-text tag="i">{{topicTitle}}</el-text>
                </el-form-item>
                <el-form-item label="掌握程度:">
                    <el-tag class="ml-2" type="success">{{practice.point.degreeOfMastery}}</el-tag>
                </el-form-item>
            </div>

            <!-- 答案区域 -->
            <div class="solution-container">
                <div class = "solution" v-show="showSkeleton">
                    <el-skeleton :rows="5" :animated=true />
                </div>
                
                <div v-show="!showSkeleton" class="solution" v-for="(subPoint, index) in practice.point.subPoints" :key="index">
                    <div class="from-label">({{ index + 1 }}){{subPoint.title}}</div>
                    <div v-html="subPoint.detailContent" class="preview-content"></div>

                </div>
                
            </div>

            <!-- 操作区域 -->
            <div class="buttons" v-if="showButton">
                <el-button type="info"  @click="toPracticeList()">列表</el-button>
                <el-button type="danger" v-if="showSkeleton" @click="practise('FORGET')">忘记</el-button>
                <el-button type="success" v-if="showSkeleton" @click="practise('DONE')">记得</el-button>
                <el-button type="warning" v-if="!showSkeleton" @click="loadNextPoint">Next</el-button>
            </div>
        </div>
    </Window>
</template>

<script lang="ts" setup>
import Window from '../../components/Window.vue';
import { ref, onMounted } from 'vue';
import {PracticeService} from '../../api/api'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router';
import {useStore} from 'vuex'
const router = useRouter();
const store = useStore()

interface SubPoint{
    title:string
    detailContent:string
}

interface Point{
    id:string;
    title: string;
    fullTitle: string;
    degreeOfMastery:string;
    subPoints: SubPoint[]
}

/** vux获取参数 */
const topicId = store.state.topicId;
const topicTitle = store.state.fullTitle;

const practice = ref({
    id:'',
    point: {} as Point,
})

// 骨架屏是否展示
const showSkeleton = ref(true);
const showButton = ref(true);

/** 主题下题目加载 */
const loadNextPoint = async () =>{
    try {
      const res = await PracticeService.getpoint({topicId}) as any;
      if(res && res.data && res.data.point){
        practice.value = res.data;
      }else{
        // 无题目，跳转至列表页
        lazyDirectList();
      }
    } catch (error) {
        console.error(error);
        lazyDirectList();
    } finally {
        showSkeleton.value = true;
    }
}
function lazyDirectList(){
    showButton.value = false;
    ElMessage.success('当前主题下已无复习题目，正在跳转至列表页～');
    setTimeout(() => {
        router.push({name:'PracticeList'});
    }, 2000);
}

onMounted(loadNextPoint);

/** 回列表 */
function toPracticeList(){
    router.push({name:'PracticeList'})
}

/**
 * 做题
 * @param result 
 */
function practise(result:string){
    PracticeService.practise({
        id:practice.value.id,
        pointId:practice.value.point.id,
        result:result
    }).then((res : any)=>{
    }).catch(err=>{
    })
    // 隐藏骨架屏
    showSkeleton.value = false;
}
</script>

<style scoped>
    .practice-container {
        display: flex;
        flex-direction: column;
        justify-content: center;
    }
    .form-wrapper {
        margin-bottom: 20px;
        margin: 20px;
        max-height: 400px;
        overflow-y: auto;
        border: 2px solid #ccc;
        border-radius: 10px;
        padding: 20px;
        background-color: #f2f2f2;
    }

    .from-item-label {
        margin-bottom: 20px;
    }

    .from-label {
        font-weight: bold;
        margin-bottom: 10px;
    }
    .buttons {
        padding: 20px;
        display: flex;
        justify-content: center;
    }
    .solution-container{
        overflow-y: auto;
        height: 35vh;
    }
    .solution{
        margin: 20px;
        border: 2px solid #ccc;
        border-radius: 10px;
        padding: 20px;
        background-color: #f2f2f2;
    }
</style>