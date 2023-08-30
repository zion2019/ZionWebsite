<template>
    <el-dialog v-model="dialogVisible" title="提示" width="60%" draggable>
    <span>确认删除:{{topic.title}}</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false" type="success">取消</el-button>
        <el-button type="danger" @click="remove" >删除</el-button>
      </span>
    </template>
  </el-dialog>
    <Window :buttons="buttons">
        <div class="edit-container">
            <div class="edit-form">
                <el-form ref="form" :model="topic" :rules="rules">
                    <div class="form-wrapper">
                        <el-form-item  prop="title">
                            <el-input v-model="topic.title" :maxlength="10" show-word-limit placeholder="主题名称"></el-input>
                        </el-form-item>
                        <el-form-item prop="parentId">
                            <el-tree-select 
                                check-strictly 
                                :props="treeProps"
                                placeholder="所属主题"
                                :data="topicTreeDatas" 
                                v-model="topic.parentId" />
                        </el-form-item>

                        <el-form-item prop="background">
                            <el-select v-model="topic.background" placeholder="背景风格">
                                <el-option  v-for="item in topicBackgrounds"  :label="item.label" :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </div>
                    <div class="form-wrapper" v-if="topic.statistic.pointCount && topic.statistic.pointCount > 0">
                        <el-form-item label="题目统计:">
                            <el-tag size="large">{{topic.statistic.pointCount}}</el-tag>
                            <el-icon :size="30" @click="listPoint">
                                <List />
                            </el-icon>
                        </el-form-item>
                        <el-form-item label="掌握程度:" v-if="topic.statistic.percentOfMastery">
                            <el-progress type="circle" :percentage="topic.statistic.percentOfMastery" />
                        </el-form-item>
                    </div>
                </el-form>
            </div>
        </div>
    </Window>
</template>

<script lang="ts" setup>
    import Window from '../../components/Window.vue'
    import { ref,onMounted,reactive } from 'vue';
    import {TopicService} from '../../api/api';
    import { topicBackgrounds } from '../../api/topic_back'
    /**  路由导入 */
    import { useRouter } from 'vue-router';
    import {useStore} from 'vuex'
    import { ElMessage } from 'element-plus'
    const store = useStore()
    const router = useRouter();

    const form = ref(null);
    // 确认删除
    const dialogVisible = ref(false)

    // 按钮定义
    const buttons = [
      {
        'type':'warning'
        ,'isPlain':true
        ,'label':'返回'
        ,'function':()=>{router.push({name:'TopicList'})}
      }
    ]
    /** vuex中获取topicID */
    const topicId = store.state.topicId;
    if(topicId){
        buttons.push({
            'type':'danger'
            ,'isPlain':false
            ,'label':'删除'
            ,'function': ()=>{dialogVisible.value = true}
        });
    }
    buttons.push({
        'type':'success'
        ,'isPlain':false
        ,'label':'添加'
        ,'function': save
    });
    

    //  选中的父主题
    const selectedParentId = ref("");

    // 类别树定义
    const treeProps = {
        children: 'children',
        label: 'label',
        value:'id',
        disabled:false
    }
    const topicTreeDatas = ref([{
        children: [] as any,
        id: '0',
        label:'ROOT',
        disabled:false
    }]);

    const topic = ref({
        title:"",
        background:"",
        parentId:"",
        statistic :{
            pointCount:0,
            percentOfMastery:0.0,
        }
    });



    onMounted(async () => {
        // 回显信息加载
        if (topicId) {
            TopicService.info(topicId).then((res:any)=>{
                topic.value = res.data;
                selectedParentId.value = topic.value.parentId
            }).catch(err=>{
            })
        }
        // 加载类别树
        TopicService.tree(topicId)
        .then((res:any)=>{
            topicTreeDatas.value[0].children = res.dataList;
        }).catch(err=>{
        })
        return topicTreeDatas;
    });

    /** 保存 */
    function save(){
        let {validate} = form.value as any 
            validate((valid:Boolean) => {
            if(valid) {
                TopicService.save(topic.value).then((res:any)=>{
                    router.push({name:"TopicList"});
                }).catch(err=>{
                    // ElMessage.error(err);
                })
            }
        });
        
    }

    const toList = () => {router.push({name:"TopicList"});}
    const listPoint = () => {
        store.commit('setTopicId',topicId);
        router.push({name:'PointList'})
    };

    /** 删除主题 */
    function remove(){
        TopicService.remove(topicId)
        .then((res: any) => {
            toList();
        })
        .catch((err: any) => {
            ElMessage.error(err.message)
        });
    }
    // 校验规则定义
    const rules = reactive({
        title: [
            { required: true, message: 'Please enter the title' },
            { pattern: /^.{1,10}$/, message: '10 characters maximum' },
        ],
        parentId: [{ required: true, message: 'Please choose a topic', trigger: 'blur' }],
        background: [{ required: true, message: 'Please choose a topic', trigger: 'blur' }],
    });


</script>

<style scoped>
    .topic-editor {
        display: flex;
        justify-content: center;
        align-items: center;
        margin-top: 10%;
    }

    /* Additional styles for form items */
    .el-form-item__label {
        color: #333;
        font-weight: bold;
    }



</style>