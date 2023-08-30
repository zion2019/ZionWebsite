<template>
    <Window :buttons="buttons">
        <List editRoutor="TopicEdit" @keywordChange="keywordChange" >
            <div class="tree-container">
                <el-tree 
                    ref="treeRef"
                    class="filter-tree"
                    default-expand-all
                    :data="listData"
                    :props="defaultProps"
                    :filter-node-method="filterNode"
                    empty-text="快创建一门主题吧"
                >
                    <template #default="{ node, data }">
                        <span class="z-tree-node">
                            <span>{{ node.label }}</span>
                            <span v-if="!node.data.readOnly">
                                <a @click="edit(node)">
                                    <el-icon><Edit /></el-icon>
                                </a>
                            </span>
                        </span>
                    </template>
                </el-tree>
            </div>
        </List>
    </Window>
</template>
<script lang="ts" setup>
import { ref,onMounted } from 'vue';
import Window from '../../components/Window.vue'
import List from '../../components/List.vue'
import { ElTree } from 'element-plus'
import {TopicService} from '../../api/api';    
import { useRouter } from 'vue-router';
import {useStore} from 'vuex'
const store = useStore()
const router = useRouter();

// 按钮定义
const buttons = [
      {
        'type':'warning'
        ,'isPlain':true
        ,'label':'返回'
        ,'function':()=>{router.push({name:'Learning'})}
      },
      {
        'type':'success'
        ,'label':'添加'
        ,'function':toNew
      }
    ]

interface Tree {
  [key: string]: any
}
const treeRef = ref<InstanceType<typeof ElTree>>();
const defaultProps = {
  children: 'children',
  label: 'label'
}

/** 树数据过滤 */
function keywordChange(val) {
  treeRef.value!.filter(val)
}
const filterNode = (value: string, data: Tree) => {
  if (!value) return true
  return data.label.includes(value)
};
const listData = ref([]);
onMounted(async () => {
    TopicService.tree('')
    .then((res:any)=>{
        listData.value = res.dataList;
    }).catch(err=>{
        // ElMessage.error(err);
    })
    return listData;
});
const edit = (node: Tree) => {
    store.commit('setTopicId',node.data.id);
    router.push({name:'TopicEdit'});
}

function toNew(){
    store.commit('setTopicId',null);
    router.push({name:'TopicEdit'});
}

</script>


<style scoped>
.z-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
  background-color: #f0f0f0;
  border: 1px solid #ddd; 
  border-radius: 5px;
}

.tree-container {
    border-radius: 10px;
    padding: 10px;
    margin: 10px 0;
    box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
}
.el-tree {
    background: #f0f0f0;
}
.el-tree-node__content{
    background: #f0f0f0;
}

</style>
