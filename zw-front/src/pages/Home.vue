<script lang="ts" setup>
    import Window from '../components/Window.vue'
    import { useRouter } from 'vue-router';
    const router = useRouter();
    const toLearing = () => {
        router.push('/learning'); // Navigate to the 'Learning' route
    };
</script>

<template>
    <Window :windowSize="0.7">
        <div class="home-container">
            <img src="../assets/imgs/Patrick.jpeg"/>
            <div class="say">
                <span>Hey, welcome to Z-Learning, I'm Zion!</span>
                <br/>
                <span>这是个主打学习+记录的网站。</span>
                <br/>
                <span>基于艾宾浩斯记忆法。</span>
                <br/>
                <span>对拆解的知识点反复练习。</span>
                <br/>
                <span>数据支持随时导出。</span>
                <br/>
                <span>使用前需要你耐心注册，不涉及隐私信息。</span>
                <br/>
                <a @click="toLearing" href="javascript:void(0);">Let's start learning ！</a>
                <br/>
            </div>
        </div>
    </Window>
</template>

<style scoped>

.home-container{
    display: flex;
    flex-direction: column;
}
.home-container > img {
    width: 70px;
    height: 70px;
    margin: 30px 0 0 45%;
    border-radius: 25%;
}

 .home-container .say {
    margin: auto;
    /* width: 400px; */
    font-family: fantasy;
    /* font-size: 19px; */
    word-break: break-word;
    white-space: pre-line;
}
    .aboutMe .say span{

    }
    .aboutMe .bar {
        display: flex;
        width: 300px;
        height: 30px;
        margin-left: 50%;
        /* background-color: red; */
    }

    .aboutMe .bar {
        display: flex;
        width: 300px;
        height: 30px;
        margin-left: 65%;
        margin-top: 10px;
    }

    .aboutMe .bar .imgthree {
        margin-left: 10px;
        margin-top: 4px;
        cursor: pointer;
    }


    .bar > a {
        margin-left: 10px;
        cursor: pointer;
    }
</style>
