<template>
  <el-dialog v-model="dialogVisible" title="提示" width="60%" draggable>
    <span>确认删除:{{point.title}}</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false" type="success">取消</el-button>
        <el-button type="danger" @click="remove" >删除</el-button>
      </span>
    </template>
  </el-dialog>
  <Window  :buttons="buttons">
    <Editor ref="EditorRef" @editorValueChange="editorValueChange" />
    <div class="edit-container">
      <div class="edit-form">
        <el-form ref="form" :model="point" label-width="80px" :rules="rules">
          <div class="form-wrapper">
            <div class="from-label">基本信息:</div>
            <el-form-item label="标题:" prop="title">
              <el-input v-model="point.title" />
            </el-form-item>
            <el-form-item label="主题:" prop="topicId">
              <el-tree-select
                check-strictly
                :props="treeProps"
                placeholder="一级分类"
                :data="topicTreeDatas"
                v-model="point.topicId"
              />
            </el-form-item>
          </div>
          <el-form v-for="(subPoint, index) in point.subPoints" :key="index" :model="subPoint" label-width="80px" :rules="rules">
            <div class="form-wrapper">
              <div class="from-label">拆解点{{ index + 1 }}:</div>
              <el-form-item label="概括:" prop="title">
                <el-input v-model="subPoint.title" placeholder="言简意赅～" />
              </el-form-item>
              <el-form-item label="内容:" prop="detailContent">
                <div v-html="subPoint.detailContent" class="preview-content"></div>
                <el-icon :size="30" @click="showEditor(index, subPoint.detailContent)">
                  <Edit />
                </el-icon>
              </el-form-item>
            </div>
          </el-form>
        </el-form>
      </div>
    </div>
  </Window>
</template>

<script lang="ts" setup>
import Window from '../../components/Window.vue';
import { ref, onMounted, reactive } from 'vue';
import Editor from '../../components/Editor.vue';
import { TopicService, PointService } from '../../api/api';
import { useRouter } from 'vue-router';
import {useStore} from 'vuex'
import { ElMessage } from 'element-plus'
const store = useStore()
// 路由变量
const router = useRouter();

// 确认删除
const dialogVisible = ref(false);

const form = ref(null);

// 树组件引用
const EditorRef = ref();
const treeProps = {
  children: 'children',
  label: 'label',
  value: 'id',
  disabled: false,
};

// 主题树结构
const topicTreeDatas = ref([]);

// 拆分点结构
interface SubPoint {
  title: string;
  detailContent: string;
}

// 知识点结构
const point = reactive({
  id: '',
  title: '',
  topicId: '',
  subPoints: [] as SubPoint[],
});

// 当前参数携带的PointId
const pointId = store.state.pointId;
point.topicId = store.state.topicId as string;

// 返回列表按钮
const buttons = [
  {
    'type':'warning'
    ,'isPlain':true
    ,'label':'列表'
    ,'function': toList
  }
]
// 删除 按钮展示逻辑
if(pointId){
  buttons.push(
    {
      'type':'danger'
      ,'isPlain':false
      ,'label':'删除'
      ,'function': ()=>{dialogVisible.value = true}
    }
  )
}
// 拆分知识点按钮
buttons.push(
    {
      'type':'info'
      ,'isPlain':false
      ,'label':'拆分'
      ,'function': addFocus
    }
)
// 拆分知识点按钮
buttons.push(
    {
      'type':'success'
      ,'isPlain':false
      ,'label':'保存'
      ,'function': submitForm
    }
)

// moundted 中回显信息以及加载主题树
onMounted(async () => {
  //  获取主题树供选择
  try {
    const topicRes = await TopicService.tree('') as any;
    topicTreeDatas.value = topicRes.dataList;
  } catch (error) {
    console.error(error);
  } 

  try {
    if(!pointId){
      return;
    }
    const pointRes = await PointService.info(pointId) as any;
    if(pointRes.data != null){
        point.id = pointRes.data.id;
        point.title = pointRes.data.title;
        point.topicId = pointRes.data.topicId;
        if(pointRes.data.subPoints != null){
            point.subPoints.push(... pointRes.data.subPoints);
        }
    }
  } catch (error) {
    console.error(error);
  } 
});

// 编辑器展示
const showEditor = (index: number, content: string) => {
  EditorRef.value.showEditor(index, content);
};

// 编辑完成回写sub-point
const editorValueChange = (index: number, content: string) => {
  point.subPoints[index].detailContent = content;
};

// 增加拆解点
function addFocus (){
  if(point.subPoints == null){
    point.subPoints = [];
  }
  point.subPoints.push({ title: '', detailContent: '' });
};

// 校验规则定义
const rules = reactive({
  title: [
    { required: true, message: '标题不能少' },
    { pattern: /^.{1,10}$/, message: '10 characters maximum' },
  ],
  topicId: [{ required: true, message: '请选择一个主题', trigger: 'blur' }],
});

// 提交表单
function submitForm(){
  let {validate} = form.value as any 
    validate((valid:Boolean) => {
      if(valid) {
        PointService.save(point)
        .then((res: any) => {
            toList();
        })
        .catch((err: any) => {
          console.error(err);
        });
      }
  });
};

function toList(){
  store.commit('setTopicId',point.topicId);
  router.push({name:"PointList"});
}

function remove (){
   dialogVisible.value = false;
    PointService.remove(pointId)
    .then((res: any) => {
        toList();
    })
    .catch((err: any) => {
      ElMessage.success(err.message)
    });
}

</script>

<style scoped>
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
</style>