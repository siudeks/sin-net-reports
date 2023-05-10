import { DetailsList, IColumn, PrimaryButton, Separator, TextField } from "@fluentui/react";
import { Link } from "react-router-dom";
import _ from "lodash";
import { useState } from "react";
import { HorizontalSeparatorStack } from "../../Components/HorizontalSeparatorStack";
import { ListCustomersItem } from "../../api/useListCustomers";

interface CustomersProps {
    givenProjectId: string,
    onNewClientCommand: () => void;
    listCustomers: (projectId: string) => ListCustomersItem[];
}


export const CustomersView: React.FC<CustomersProps> = (props) => {

    interface TypedColumn extends IColumn {
        fieldName: keyof ListCustomersItem;
    }

    const items = props.listCustomers(props.givenProjectId);
    const [searchPhrase, setSearchPhrase] = useState<string | undefined>('');

    const columns: TypedColumn[] = [
        {
            key: "column1", name: "Klient", fieldName: "name", minWidth: 70, maxWidth: 90, isResizable: true, isCollapsible: true, data: "string",
            onRender: (item: ListCustomersItem) => {
                return <Link to={`/customers/${item.customerId.projectId}/${item.customerId.entityId}/${item.customerId.entityVersion}`}>{item.name}</Link>;
            },
            isPadded: true
        }
    ];

    const similarity = (actual: string, template?: string): number => {
        if (!actual) return 0;
        if (!template) return 1;
        const lowerActual = actual.toLowerCase();
        const lowerTemplate = template.toLowerCase().trim();
        if (lowerTemplate === '') return 1;
        if (lowerActual === lowerTemplate) return 100;
        if (lowerActual.startsWith(lowerTemplate)) return 80;
        if (lowerActual.includes(' ' + lowerTemplate)) return 60;
        if (lowerActual.includes(lowerTemplate)) return 40;
        return 0;
    }

    const sortedItems = _.chain(items)
        .map(it => ({ ...it, sortPriority: similarity(it.name, searchPhrase) + similarity(it.termNfzKodSwiadczeniodawcy, searchPhrase) }))
        .filter(it => it.sortPriority > 0)
        .orderBy(it => it.name)
        .orderBy(it => -it.sortPriority)
        .value();

    return (
        <div className="ms-Grid">
            <HorizontalSeparatorStack >
                <div className="ms-Grid-row">
                    <div className="ms-Grid-col ms-sm3" style={{ padding: 10 }}>
                        <PrimaryButton text="Dodaj nowego klienta" onClick={() => props.onNewClientCommand()} />
                    </div>
                </div>
                <div className="ms-Grid-row">
                    <div className="ms-Grid-col ms-sm12">
                        <TextField placeholder="Wprowadź fragment nazwy klienta ..." value={searchPhrase} onChange={e => setSearchPhrase((e.target as any).value)} />
                    </div>
                </div>

                <div className="ms-Grid-row">

                    <Separator alignContent="start"></Separator>

                    <div className="ms-Grid-col ms-sm12">
                        <DetailsList
                            items={sortedItems}
                            compact={true}
                            columns={columns}
                            setKey="none"
                            isHeaderVisible={true}
                        />
                    </div>
                </div>

            </HorizontalSeparatorStack>
        </div >
    )
}
