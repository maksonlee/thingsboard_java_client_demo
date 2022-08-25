import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.page.PageData;
import org.thingsboard.server.common.data.query.*;
import org.thingsboard.server.common.data.relation.EntitySearchDirection;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AssetSearchQuery {
    public static void main(String[] args) {
        String url = "http://localhost:8080";

        String username = "username";
        String password = "password";
        RestClient client = new RestClient(url);
        client.login(username, password);

        //entityFields
        List<EntityKey> fields = List.of(
                new EntityKey(EntityKeyType.ENTITY_FIELD, "name")
        );

        //entityFilter
        AssetSearchQueryFilter queryFilter = new AssetSearchQueryFilter();
        queryFilter.setRootEntity(new EntityId() {
            @Override
            public UUID getId() {
                return UUID.fromString("58133560-961b-11ec-88b5-55ce454570bd");
            }

            @Override
            public EntityType getEntityType() {
                return EntityType.USER;
            }
        });
        queryFilter.setDirection(EntitySearchDirection.FROM);
        queryFilter.setMaxLevel(2);
        queryFilter.setFetchLastLevelOnly(false);
        queryFilter.setRelationType("Contains");
        queryFilter.setAssetTypes(Arrays.asList("Room"));

        //keyFilters
        KeyFilter keyFilter = new KeyFilter();
        keyFilter.setKey(new EntityKey(EntityKeyType.ATTRIBUTE, "floorId"));
        keyFilter.setValueType(EntityKeyValueType.STRING);

        StringFilterPredicate filterPredicate = new StringFilterPredicate();
        filterPredicate.setOperation(StringFilterPredicate.StringOperation.EQUAL);
        filterPredicate.setValue(new FilterPredicateValue<>("2"));

        keyFilter.setPredicate(filterPredicate);

        //latestValues
        List<EntityKey> attributes = List.of(
                new EntityKey(EntityKeyType.ATTRIBUTE, "floorId"),
                new EntityKey(EntityKeyType.ATTRIBUTE, "sickroomId"),
                new EntityKey(EntityKeyType.ATTRIBUTE, "sipNumber"),
                new EntityKey(EntityKeyType.ATTRIBUTE, "sipPassword"),
                new EntityKey(EntityKeyType.ATTRIBUTE, "residentName"),
                new EntityKey(EntityKeyType.ATTRIBUTE, "normalStatus"),
                new EntityKey(EntityKeyType.ATTRIBUTE, "attentionStatus"),
                new EntityKey(EntityKeyType.ATTRIBUTE, "warningStatus"),
                new EntityKey(EntityKeyType.ATTRIBUTE, "access_monitoring"),
                new EntityKey(EntityKeyType.ATTRIBUTE, "dismissed")
        );

        //pageLink
        EntityDataSortOrder sortOrder = new EntityDataSortOrder();
        sortOrder.setKey(new EntityKey(EntityKeyType.ENTITY_FIELD, "name"));
        sortOrder.setDirection(EntityDataSortOrder.Direction.ASC);
        EntityDataPageLink entityDataPageLink = new EntityDataPageLink(9999, 0, "", sortOrder);

        //query
        EntityDataQuery dataQuery =
                new EntityDataQuery(queryFilter, entityDataPageLink, fields, attributes, List.of(keyFilter));

        //fetch data
        PageData<EntityData> entityPageData;
        do {
            entityPageData = client.findEntityDataByQuery(dataQuery);
            entityPageData.getData().forEach(System.out::println);
            dataQuery = dataQuery.next();
        } while (entityPageData.hasNext());

        client.logout();
        client.close();
    }
}
