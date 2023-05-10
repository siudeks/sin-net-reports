package sinnet.gql.models;

import java.time.LocalDate;

public interface Mapper {
    default sinnet.grpc.timeentries.LocalDate toGrpc(LocalDate value) {
        if (value == null) return null;
        return sinnet.grpc.timeentries.LocalDate.newBuilder()
            .setYear(value.getYear())
            .setMonth(value.getMonthValue())
            .setDay(value.getDayOfMonth())
            .build();
    }

    default LocalDate toGql(sinnet.grpc.timeentries.LocalDate value) {
        if (value == null) return null;
        return LocalDate.of(value.getYear(), value.getMonth(), value.getDay());
    }

    default Entity toGql(sinnet.grpc.timeentries.ReserveResult value) {
        if (value == null) return null;
        var result = new Entity();
        var eid = value.getEntityId();
        result.setProjectId(eid.getProjectId());
        result.setEntityId(eid.getEntityId());
        result.setEntityVersion(eid.getEntityVersion());
        return result;
    }
}
