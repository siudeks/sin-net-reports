package sinnet.gql.models;

import lombok.Data;

/** Fixme. */
@Data
public class CustomerModelGql {
  private String operatorEmail;
  private String billingModel;
  private String supportStatus;
  private Integer distance;
  private String customerName;
  private String customerCityName;
  private String customerAddress;
  private Boolean nfzUmowa;
  private Boolean nfzMaFilie;
  private Boolean nfzLekarz;
  private Boolean nfzPolozna;
  private Boolean nfzPielegniarkaSrodowiskowa;
  private Boolean nfzMedycynaSzkolna;
  private Boolean nfzTransportSanitarny;
  private Boolean nfzNocnaPomocLekarska;
  private Boolean nfzAmbulatoryjnaOpiekaSpecjalistyczna;
  private Boolean nfzRehabilitacja;
  private Boolean nfzStomatologia;
  private Boolean nfzPsychiatria;
  private Boolean nfzSzpitalnictwo;
  private Boolean nfzProgramyProfilaktyczne;
  private Boolean nfzZaopatrzenieOrtopedyczne;
  private Boolean nfzOpiekaDlugoterminowa;
  private String nfzNotatki;
  private Boolean komercjaJest;
  private String komercjaNotatki;
  private String daneTechniczne;  
}
