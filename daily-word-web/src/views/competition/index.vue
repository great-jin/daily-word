<template>
  <div>
    <span style="margin-right: 6px">
      词典:
    </span>
    <el-select
        v-model="reqParams.catalogue"
        placeholder="Select dictionary"
        style="width: 160px; margin-right: 20px"
    >
      <el-option
          v-for="item in catalogues"
          :key="item"
          :label="item"
          :value="item"
      />
    </el-select>

    <span style="margin-right: 6px">
      数量:
    </span>
    <el-input-number
        v-model="reqParams.size"
        :min="5"
        :max="50"
        placeholder="Select dictionary"
        style="width: 160px; margin-right: 20px"
    />

    <el-button type="primary" @click="startTask()">开始答题</el-button>

    <AnswerDialog ref="answerDialog"/>
  </div>
</template>

<script>
import AnswerDialog from "./answerDialog.vue";
import {getTaskContent} from "@/api/wordApi";

export default {
  components: {
    AnswerDialog
  },
  inject: ['reload'],
  data() {
    return {
      catalogues: ['CET4', 'CET6', 'GRE', 'Graduate', 'Oxford'],
      reqParams: {
        catalogue: 'CET4',
        size: 5,
        offset: 10
      }
    }
  },
  methods: {
    startTask() {
      getTaskContent(this.reqParams).then(res => {
        this.$refs.answerDialog.show(res.data);
      })
    }
  }
}
</script>
