<template>
    <div class="editor" v-show="isShow">
        <el-text class="mx-1" size="large">正在编辑：xxx</el-text>
        <div class="editor-primary">
            <!-- 此处注意写法v-model:content -->
            <QuillEditor ref="myQuillEditor"
            theme="snow"
            v-model:content="content"
            :options="data.editorOption"
            contentType="html"
            @update:content="setValue()"
            />
            <!-- 使用自定义图片上传 -->
            <input type="file" hidden accept=".jpg,.png" ref="fileBtn"  />
        </div>
        <div class = "option">
            <el-button type="success"  @click="closeEditor">完成</el-button>
        </div>
    </div>
</template>

<script lang="ts" setup>
    import { reactive, onMounted, ref, toRaw, watch } from 'vue'
    import { QuillEditor } from '@vueup/vue-quill'
    import '@vueup/vue-quill/dist/vue-quill.snow.css'
    const isShow = ref(false);
    const innerIndex = ref();
    const props = defineProps(['value'])
    const emit = defineEmits(['editorValueChange'])
    const content = ref('')
    const myQuillEditor = ref()
    

    function showEditor(index:number,extraContent : string ){
        // 回显已有内容
        const quill = toRaw(myQuillEditor.value).getQuill()
        quill.setContents(quill.clipboard.convert(extraContent))
        innerIndex.value = index;
        // 展示编辑窗口
        isShow.value = true;
    }

    function closeEditor(){
        // 回传父组件并且清空内容
        emit('editorValueChange',innerIndex.value,content.value);
        const quill = toRaw(myQuillEditor.value).getQuill()
        quill.setContents(quill.clipboard.convert(''))
        // 隐藏编辑窗口
        isShow.value = false;
    }

    // 主动暴露childMethod方法
    defineExpose({ showEditor })

    // 通过watch监听回显，笔者这边使用v-model:content 不能正常回显
    const fileBtn = ref()
    const data = reactive({
    content: '',
    editorOption: {
        modules: {
        toolbar: [
            ['bold', 'italic', 'underline', 'strike'],
            [{ 'size': ['small', false, 'large', 'huge'] }],
            [{ 'font': [] }],
            [{ 'align': [] }],
            [{ 'list': 'ordered' }, { 'list': 'bullet' }],
            [{ 'indent': '-1' }, { 'indent': '+1' }],
            [{ 'header': 1 }, { 'header': 2 }],
            ['image'],
            [{ 'direction': 'rtl' }],
            [{ 'color': [] }, { 'background': [] }]
        ]
        },
        placeholder: '请输入内容...'
    }
    })
    const imgHandler = (state) => {
    if (state) {
        fileBtn.value.click()
    }
    }
    // 抛出更改内容，此处避免出错直接使用文档提供的getHTML方法
    const setValue = () => {
        const text = toRaw(myQuillEditor.value).getHTML()
    }
    // const handleUpload = (e) => {
    // const files = Array.prototype.slice.call(e.target.files)
    // // console.log(files, "files")
    // if (!files) {
    //     return
    // }
    // const formdata = new FormData()
    // formdata.append('file', files[0])
    // backsite.uploadFile(formdata)  // 此处使用服务端提供上传接口
    //     .then(res => {
    //     if (res.data.url) {
    //         const quill = toRaw(myQuillEditor.value).getQuill()
    //         const length = quill.getSelection().index
    //         quill.insertEmbed(length, 'image', res.data.url)
    //         quill.setSelection(length + 1)
    //     }
    //     })
    // }
    // 初始化编辑器
    onMounted(() => {
        const quill = toRaw(myQuillEditor.value).getQuill()
        if (myQuillEditor.value) {
            quill.getModule('toolbar').addHandler('image', imgHandler)
        }
    })

</script>

<style scoped>
    .editor {
        position: absolute;
        z-index: 2;
        display: flex;
        flex-direction: column;
        justify-content: center;
        height: 95%;
        width: 100%;
        background: #f0f0f0;
    }
    .editor-primary{
        height: 90%;
        display: flex;
        flex-direction: column
    }
    .option{
        display: flex;
        justify-content: center;
    }
    :deep(.ql-editor) {
        min-height: 180px;
    }
    :deep(.ql-formats) {
        height: 21px;
        line-height: 21px;
    }
</style>