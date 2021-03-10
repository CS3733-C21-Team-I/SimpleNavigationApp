package edu.wpi.cs3733.c21.teamI.hospitalMap;

public enum NodeRestrictions {
  PPE_REQUIRED,
  STAFF_ONLY,
  WHEELCHAIR_INACCESSIBLE,
  NON_COVID_RISK_VISITORS, // used for atrium lobby entrance
  COVID_RISK_VISITORS // used for emergency entrance
}
