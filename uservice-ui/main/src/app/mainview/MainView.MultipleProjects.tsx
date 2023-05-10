import React from "react";
import { ChoiceGroup, IChoiceGroupOption, DefaultButton, Stack } from "@fluentui/react";
import _ from "lodash";

interface MainViewMultipleProjectsProps {
    projects: { id: string, name: string }[]
    projectSelected: (id: string) => void
}
export const MainViewMultipleProjects: React.FC<MainViewMultipleProjectsProps> = props => {

    const options: IChoiceGroupOption[] = _.chain(props.projects)
        .map(it => ({ key: it.id, text: it.name }))
        .value();

    const [selected, setSelected] = React.useState<string | undefined>();

    return (
        <Stack>
            <Stack.Item align="center">
                <span>
                    <ChoiceGroup options={options} label="Wybierz projekt na którym chcesz pracować:" onChange={(e, v) => setSelected(v?.key)} />
                </span>
                <span>
                    <DefaultButton disabled={selected ? false : true}
                        onClick={e => props.projectSelected(selected!)}>Kontunuuj pracę z wybranym projektem</DefaultButton>
                </span>
            </Stack.Item>
        </Stack>
    );
}

