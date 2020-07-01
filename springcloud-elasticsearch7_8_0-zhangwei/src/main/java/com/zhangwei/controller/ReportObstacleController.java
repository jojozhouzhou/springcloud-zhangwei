package com.zhangwei.controller;

import com.zhangwei.bean.ReportObstacle;
import com.zhangwei.utils.Defs;
import com.zhangwei.utils.JsonUtils;
import com.zhangwei.utils.RandomUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author zhangwei
 * @date 2020-06-28
 * <p>
 */
@RestController
@RequestMapping(value = "/es")
@Api(value = "ES全文搜索", description = "ES全文搜索")
public class ReportObstacleController {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引
     *
     * @param indexName
     * @param mappings
     * @return
     * @throws Exception
     */
    @GetMapping("/createIndex")
    @PostMapping(value = "/create")
    @ApiOperation(value = "创建索引", tags = "ES全文搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexName", value = "索引名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mappings", value = "映射", required = true, dataType = "String", paramType = "query")
    })
    public String createIndex(String indexName, String mappings) throws Exception {
        CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
        Settings build = Settings.builder()
                .put("number_of_shards", "5")
                .put("number_of_replicas", "1")
                .build();
        indexRequest.settings(build);
        indexRequest.mapping(mappings, XContentType.JSON);
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(indexRequest, RequestOptions.DEFAULT);
        boolean acknowledged = createIndexResponse.isAcknowledged();
        indexName += acknowledged ? ",创建成功" : ",创建失败";
        return indexName;
    }

    /**
     * 创建文档
     *
     * @param
     * @return
     */
    @PostMapping(value = "/addDocumnet")
    @ApiOperation(value = "添加文档", tags = "ES全文搜索")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "reportObstacle", value = "文档内容", required = true, dataType = "ReportObstacle", paramType = "body")
    })
    public String addDocumnet() throws Exception {
        long start = System.currentTimeMillis();
        StringBuilder contents = new StringBuilder("中共中央政治局召开会议" +
                "审议《中国共产党军队党的建设条例》和《中国共产党基层组织选举工作条例》" +
                "中共中央总书记习近平主持会议" +
                "新华社北京6月29日电 中共中央政治局6月29日召开会议，审议《中国共产党军队党的建设条例》和《中国共产党基层组织选举工作条例》。中共中央总书记习近平主持会议。" +
                "会议指出，党的领导和党的建设是人民军队建设发展的关键，关系强军事业兴衰成败，关系党和国家长治久安。制定《中国共产党军队党的建设条例》，是深入贯彻习近平新时代中国特色社会主义思想和党的十九大精神的重要举措，对增强“四个意识”、坚定“四个自信”、做到“两个维护”，贯彻军委主席负责制，对确保党对军队绝对领导，确保有效履行新时代军队使命任务，确保人民军队永葆性质、宗旨、本色，对实现党在新时代的强军目标、把人民军队全面建成世界一流军队，具有重要意义。" +
                "会议强调，要毫不动摇坚持党对军队绝对领导，全面深入贯彻军委主席负责制，持续深化政治整训，做到绝对忠诚、绝对纯洁、绝对可靠。要坚持聚焦聚力备战打仗，把战斗力这个唯一的根本的标准贯彻到人民军队党的建设全过程和各方面，推动党的政治优势和组织优势转化为制胜优势。要坚持贯彻全面从严治党要求，坚定不移正风肃纪，坚决纠治形式主义、官僚主义。要坚持勇于自我革命，不断自我净化、自我完善、自我革新、自我提高。" +
                "会议要求，要加强《中国共产党军队党的建设条例》学习宣传和贯彻落实。要突出书记队伍和班子成员，抓好学习培训，提高抓党的建设的意识和能力。要强化领导督导，确保《中国共产党军队党的建设条例》有效执行、落地见效。" +
                "会议指出，党的十八大以来，以习近平同志为核心的党中央高度重视基层党组织选举工作，对坚持和加强党的全面领导、贯彻执行民主集中制、规范完善党内选举制度作出一系列重大部署，有力推动了基层党组织建设。制定和实施《中国共产党基层组织选举工作条例》，是落实党要管党、全面从严治党要求，是发扬党内民主、尊重党员民主权利、规范基层党组织选举的具体举措，对增强基层党组织政治功能和组织力，把基层党组织建设成为宣传党的主张、贯彻党的决定、领导基层治理、团结动员群众、推动改革发展的坚强战斗堡垒，巩固党长期执政的组织基础，具有重要意义。" +
                "会议强调，要严格执行选举制度规定，提高党内选举质量，保障党章赋予的党员权利，增强党的意识、政治意识、规矩意识。要严格代表资格条件，确保选出合格的代表。要合理分配代表名额，优化代表结构，确保生产和工作一线代表比例。要按照德才兼备、以德为先和班子结构合理的原则提名委员候选人。要坚持教育在先、警示在先、预防在先，严肃政治纪律、组织纪律和换届纪律，确保选举风清气正。" +
                "会议要求，各级党委要加强对《中国共产党基层组织选举工作条例》实施的组织领导，严格落实党委主体责任，加强谋划，精心组织，全程把关。要贯彻执行民主集中制，教育引导党员和代表正确行使民主权利，保证选举工作平稳有序。要强化基层党组织书记和党务骨干培训，提升工作规范化水平。要加强督促落实，确保《中国共产党基层组织选举工作条例》各项规定要求落到实处。" +
                "会议还研究了其他事项。");
        int size = contents.length();
        long dist = 100000L;

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueSeconds(10));

        for (int i = 0; i < dist; i++) {
            ReportObstacle reportObstacle = new ReportObstacle();
            reportObstacle.setId(System.currentTimeMillis());
            reportObstacle.setTroubleNo(System.currentTimeMillis());
            reportObstacle.setProblemTitle(UUID.randomUUID().toString());
            int between = RandomUtils.getBetween(0, size);
            reportObstacle.setProblemDesc(contents.substring(between));
            reportObstacle.setTroubleTime(System.currentTimeMillis());
            between = between < 4 ? 4 : between;
            reportObstacle.setSystemName(contents.substring(between - 4, between));
            between = between >= size ? size - 2 : between;
            reportObstacle.setModuleName(between >= size * 0.5 ? contents.substring(between - 2, between) : "");

            bulkRequest.add(new IndexRequest(Defs.ES_INDEX_NAME)
                    .id("" + (i + 1))
                    .source(Objects.requireNonNull(JsonUtils.toJson(reportObstacle)), XContentType.JSON));
        }

        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        long end = System.currentTimeMillis();
        return String.format("ES在索引%s中, 生成%s条数据%s, 耗时: %s秒", Defs.ES_INDEX_NAME, dist, !bulk.hasFailures(), (end - start) / 1000);
    }
}
