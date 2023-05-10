import moment from "moment";

export interface SecretsTimestamp {
    value: string;
}

/** Build from UTD datetime, forematted to dd/MM/yyyy hh:mm */
export class SecretsTimestamp {
    public static of(utc: string) : SecretsTimestamp {
        var stillUtc = moment.utc(utc).toDate()
        // more https://momentjscom.readthedocs.io/en/latest/moment/04-displaying/01-format/
        var local = moment(stillUtc).format("DD/MM/yyyy HH:mm");
        return { value: local};
    }
}
