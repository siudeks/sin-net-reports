import { ServiceItemFragment } from "../Components/.generated/components";
import { ServiceAppModel } from "../store/actions/ServiceModel";
import { LocalDate } from "../store/viewcontext/TimePeriod";

export const toModel = (dtoDate: string): LocalDate => {
    const yearAsString = dtoDate.substring(0, 4);
    const monthAsString = dtoDate.substring(5, 7);
    const dayAsString = dtoDate.substring(8, 10);
    const year = Number(yearAsString);
    const month = Number(monthAsString);
    const day = Number(dayAsString);

    return { year, month, day };
}

export const toActionModel = (dto: ServiceItemFragment): ServiceAppModel => {
    const item = {
        projectId: dto.projectId,
        entityId: dto.entityId,
        entityVersion: dto.entityVersion,
        description: dto.description ?? undefined,
        servicemanName: dto.servicemanName ?? undefined,
        servicemanEmail: dto.servicemanEmail ?? undefined,
        customerName: dto.customer?.data.customerName ?? undefined,
        when: toModel(dto.whenProvided),
        distance: dto.distance ?? undefined,
        duration: dto.duration ?? undefined,
        customer: {
            id: { 
                projectId: dto.customer?.id.projectId,
                entityId: dto.customer?.id.entityId,
                entityVersion: dto.customer?.id.entityVersion
            },
            name: dto.customer?.data.customerName
        }
    }
    return item as ServiceAppModel;
}

export const dates = (it: LocalDate | null | undefined) => {
    if (!it) {
        const now = new Date();
        it = {
            year: now.getFullYear(),
            month: now.getMonth() + 1,
            day: now.getDay()
        }
    }
    return {
        noSeparator: rawValue(it, ''),
        slashed: rawValue(it, '/')
    };
}

const rawValue = (it: LocalDate, separator: string) => {
    return ('0000' + it.year).substr(-4) + separator +
        ('00' + it.month).substr(-2) + separator + 
        ('00' + it.day).substr(-2);
}

