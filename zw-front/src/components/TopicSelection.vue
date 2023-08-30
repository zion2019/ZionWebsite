<template>
    <div>
        <el-tree-select 
            check-strictly 
            :props="treeProps"
            placeholder="一级分类"
            :data="topicTreeDatas" 
            @node-click="handleNodeClick"
            v-model="changeSelectedValue" />
            <el-button type="info" :icon="Delete" @click="clear" circle />
    </div>
    
</template>

<script lang="ts" setup>
import {TopicService} from '../api/api';
import { ref,onMounted,toRef, onUpdated } from 'vue';
import {Delete,} from '@element-plus/icons-vue'
import { useRoute } from 'vue-router';
const  emit  = defineEmits(["input"]);
const route = useRoute();

const props = defineProps({
  value: String, 
});
const selectedValue = toRef(props, 'value');

const changeSelectedValue = ref();


const treeProps = {
    children: 'children',
    label: 'label',
    value:'id',
    disabled:false
}

const handleNodeClick = (node) => {
    emit("input", node.id);
}


const clear = ()=>{
    changeSelectedValue.value = "";
}

const topicTreeDatas = ref([]);
onMounted(async () => {
    console.log('mounted',props.value)
    
    let topicId = route.query.topicId;
    TopicService.tree(topicId)
    .then((res:any)=>{
        topicTreeDatas.value = res.dataList;
    }).catch(err=>{
        // ElMessage.error(err);
    })
    return topicTreeDatas;
});

</script>