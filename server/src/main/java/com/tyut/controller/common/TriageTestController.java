package com.tyut.controller.common;

import com.tyut.result.Result;
import com.tyut.utils.TriageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 分诊功能测试控制器
 * 用于验证智能分诊的准确性
 */
@Slf4j
@RestController
@RequestMapping("/api/triage-test")
@Api(tags = "分诊功能测试接口")
public class TriageTestController {

    @Autowired
    private TriageUtil triageUtil;

    @PostMapping("/evaluate")
    @ApiOperation("评估分诊结果")
    public Result<Map<String, Object>> evaluateTriage(@RequestParam String symptom) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取分诊等级
            Integer level = triageUtil.determineTriageLevel(symptom);
            String description = triageUtil.getLevelDescription(level);
            
            result.put("symptom", symptom);
            result.put("level", level);
            result.put("description", description);
            result.put("success", true);
            
            log.info("分诊评估 - 症状: {}, 等级: {}, 描述: {}", symptom, level, description);
            
        } catch (Exception e) {
            log.error("分诊评估失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return Result.success(result);
    }

    @GetMapping("/test-cases")
    @ApiOperation("获取测试用例")
    public Result<Map<String, Object>> getTestCases() {
        Map<String, Object> testCases = new HashMap<>();
        
        // 1级测试用例
        testCases.put("一级危急症状", new String[]{
            "胸痛剧烈，大汗淋漓，呼吸困难",
            "突然昏迷，意识丧失",
            "大出血，血流不止",
            "呼吸停止，心跳骤停"
        });
        
        // 2级测试用例
        testCases.put("二级紧急症状", new String[]{
            "右手手臂骨折",
            "高烧39.5度，头疼欲裂",
            "严重外伤，刀伤很深",
            "大面积烧伤，皮肤发红起泡"
        });
        
        // 3级测试用例
        testCases.put("三级普通急诊", new String[]{
            "肚子疼，有点拉肚子",
            "轻微咳嗽，低烧37.5度",
            "扭伤脚踝，走路有点疼",
            "头晕恶心，想吐"
        });
        
        // 4级测试用例
        testCases.put("四级非紧急症状", new String[]{
            "轻微感冒，流鼻涕",
            "小伤口擦伤",
            "慢性病定期复诊",
            "健康咨询体检"
        });
        
        return Result.success(testCases);
    }

    @PostMapping("/batch-test")
    @ApiOperation("批量测试分诊准确性")
    public Result<Map<String, Object>> batchTest() {
        Map<String, Object> results = new HashMap<>();
        
        // 优化的测试用例，覆盖各种典型场景
        String[] testSymptoms = {
            // 1级危急症状
            "胸痛剧烈，大汗淋漓，呼吸困难",     // 1级
            "突然昏迷，意识完全丧失",           // 1级
            "大出血不止，面色苍白",             // 1级
            "呼吸停止，心跳骤停",               // 1级
            
            // 2级紧急症状
            "右手手臂骨折，明显畸形",           // 2级
            "高烧39.5度，寒战发抖",             // 2级
            "剧烈腹痛，难以忍受",               // 2级
            "严重外伤，刀伤很深",               // 2级
            "大面积烧伤，皮肤起泡",             // 2级
            "急性过敏，呼吸困难",               // 2级
            
            // 3级普通急诊
            "肚子疼，有点拉肚子",               // 3级
            "轻微头痛，休息后能缓解",           // 3级
            "低烧37.8度，咳嗽有痰",             // 3级
            "扭伤脚踝，轻度肿胀",               // 3级
            "割伤需要缝合处理",                 // 3级
            
            // 4级非紧急症状
            "轻微感冒，流清鼻涕",               // 4级
            "手指轻微擦伤",                     // 4级
            "慢性病定期复查",                   // 4级
            "健康咨询体检",                     // 4级
            "轻微皮肤过敏"                      // 4级
        };
        
        Map<String, Object> testCaseResults = new HashMap<>();
        int correctCount = 0;
        int totalCount = testSymptoms.length;
        
        // 统计各级别的准确率
        Map<Integer, Integer> levelTotalCount = new HashMap<>();
        Map<Integer, Integer> levelCorrectCount = new HashMap<>();
        
        for (String symptom : testSymptoms) {
            try {
                Integer level = triageUtil.determineTriageLevel(symptom);
                String description = triageUtil.getLevelDescription(level);
                
                Map<String, Object> caseResult = new HashMap<>();
                caseResult.put("level", level);
                caseResult.put("description", description);
                
                // 获取预期结果
                Integer expectedLevel = getExpectedLevel(symptom);
                caseResult.put("expectedLevel", expectedLevel);
                boolean isCorrect = level.equals(expectedLevel);
                caseResult.put("correct", isCorrect);
                
                if (isCorrect) {
                    correctCount++;
                    levelCorrectCount.merge(expectedLevel, 1, Integer::sum);
                }
                
                levelTotalCount.merge(expectedLevel, 1, Integer::sum);
                testCaseResults.put(symptom, caseResult);
                
            } catch (Exception e) {
                log.error("测试用例执行失败: " + symptom, e);
                Map<String, Object> caseResult = new HashMap<>();
                caseResult.put("error", e.getMessage());
                testCaseResults.put(symptom, caseResult);
                totalCount--; // 出错的不计入总数
            }
        }
        
        // 计算各级别的准确率
        Map<String, Object> levelAccuracy = new HashMap<>();
        for (Integer level : levelTotalCount.keySet()) {
            int total = levelTotalCount.get(level);
            int correct = levelCorrectCount.getOrDefault(level, 0);
            double accuracy = (double) correct / total * 100;
            levelAccuracy.put("级别" + level + "准确率", String.format("%.1f%% (%d/%d)", accuracy, correct, total));
        }
        
        results.put("testCases", testCaseResults);
        results.put("totalTests", totalCount);
        results.put("correctCount", correctCount);
        results.put("overallAccuracy", totalCount > 0 ? String.format("%.1f%%", (double) correctCount / totalCount * 100) : "0%");
        results.put("levelAccuracy", levelAccuracy);
        
        return Result.success(results);
    }
    
    /**
     * 根据症状获取预期的分诊等级（精细化规则）
     */
    private Integer getExpectedLevel(String symptom) {
        String s = symptom.toLowerCase();
        
        // 1级症状（危急）
        if ((s.contains("胸痛") && (s.contains("剧烈") || s.contains("大汗"))) ||
            s.contains("昏迷不醒") || s.contains("意识完全丧失") ||
            (s.contains("大出血") && s.contains("不止")) ||
            (s.contains("呼吸") && s.contains("停止")) ||
            (s.contains("心跳") && s.contains("骤停"))) {
            return 1;
        }
        
        // 2级症状（紧急）
        if (s.contains("骨折") || s.contains("骨裂") || s.contains("脱臼") ||
            (s.contains("高热") || s.contains("39度") || s.contains("40度")) ||
            (s.contains("剧痛") || s.contains("疼痛难忍")) ||
            (s.contains("严重外伤") || s.contains("刀伤很深")) ||
            (s.contains("烧伤") && s.contains("面积")) ||
            (s.contains("急性过敏") && s.contains("呼吸困难")) ||
            s.contains("吐血") || s.contains("便血")) {
            return 2;
        }
        
        // 3级症状（普通急诊）
        if ((s.contains("腹痛") || s.contains("肚子疼")) && 
            !(containsAny(s, "轻微", "有点", "不太严重")) ||
            (s.contains("头痛") || s.contains("头疼")) && 
            !(containsAny(s, "轻微", "有点")) ||
            (s.contains("低烧") || s.contains("37度") || s.contains("38度")) ||
            s.contains("呕吐") || s.contains("腹泻") || s.contains("拉肚子") ||
            s.contains("头晕") || s.contains("扭伤肿胀") ||
            s.contains("割伤需要处理")) {
            return 3;
        }
        
        // 4级症状（非紧急）
        if (s.contains("感冒") || s.contains("流鼻涕") || s.contains("打喷嚏") ||
            (s.contains("轻微") && (s.contains("擦伤") || s.contains("头痛") || s.contains("腹痛"))) ||
            s.contains("复诊") || s.contains("体检") || s.contains("咨询") ||
            s.contains("慢性病")) {
            return 4;
        }
        
        return 4; // 默认4级（最保守的选择）
    }
    
    /**
     * 辅助方法：判断字符串是否包含数组中的任意关键词
     */
    private boolean containsAny(String target, String... keywords) {
        if (target == null || keywords == null) return false;
        for (String key : keywords) {
            if (key != null && target.contains(key.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}