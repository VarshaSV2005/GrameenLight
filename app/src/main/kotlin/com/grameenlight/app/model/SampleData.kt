package com.grameenlight.app.model

object SampleData {

    val poles = mutableListOf(
        Pole("PL-001", "Main Bazaar Road",   13.0601, 77.7301, 0.18f, 0.22f, PoleStatus.WORKING,     RepairStatus.NONE,     lastReportedMillis = System.currentTimeMillis() - 172_800_000),
        Pole("PL-002", "Main Bazaar Road",   13.0605, 77.7320, 0.32f, 0.22f, PoleStatus.FUSED,       RepairStatus.FIXED,    assignedTo = "Ward 3 Electrician", complaintId = "CMP-2841", lastReportedMillis = System.currentTimeMillis() - 86_400_000),
        Pole("PL-003", "Gandhi Nagar",       13.0620, 77.7340, 0.55f, 0.35f, PoleStatus.BURNING_IN_DAY, RepairStatus.ASSIGNED, assignedTo = "Panchayat Office",   complaintId = "CMP-2837", lastReportedMillis = System.currentTimeMillis() - 10_800_000),
        Pole("PL-004", "Gandhi Nagar",       13.0625, 77.7355, 0.70f, 0.35f, PoleStatus.WORKING,     RepairStatus.NONE,     lastReportedMillis = System.currentTimeMillis() - 432_000_000),
        Pole("PL-005", "Temple Street",      13.0590, 77.7325, 0.25f, 0.58f, PoleStatus.FUSED,       RepairStatus.ASSIGNED, assignedTo = "Ravi Kumar (Lineman)", complaintId = "CMP-2839", lastReportedMillis = System.currentTimeMillis() - 21_600_000),
        Pole("PL-006", "Temple Street",      13.0588, 77.7340, 0.40f, 0.58f, PoleStatus.WORKING,     RepairStatus.NONE,     lastReportedMillis = System.currentTimeMillis() - 86_400_000),
        Pole("PL-007", "School Road",        13.0575, 77.7350, 0.60f, 0.68f, PoleStatus.BURNING_IN_DAY, RepairStatus.PENDING,  lastReportedMillis = System.currentTimeMillis() - 1_800_000),
        Pole("PL-008", "School Road",        13.0572, 77.7365, 0.78f, 0.68f, PoleStatus.WORKING,     RepairStatus.NONE,     lastReportedMillis = System.currentTimeMillis() - 345_600_000),
        Pole("PL-009", "Panchayat Road",     13.0560, 77.7310, 0.15f, 0.80f, PoleStatus.WORKING,     RepairStatus.NONE,     lastReportedMillis = System.currentTimeMillis() - 172_800_000),
        Pole("PL-010", "Panchayat Road",     13.0558, 77.7370, 0.82f, 0.50f, PoleStatus.FUSED,       RepairStatus.PENDING,  lastReportedMillis = System.currentTimeMillis() - 43_200_000)
    )

    val complaints = mutableListOf(
        Complaint("CMP-2841", "PL-002", "Main Bazaar Road", "Fused",         RepairStatus.FIXED,    "Ward 3 Electrician",  "2 days ago"),
        Complaint("CMP-2839", "PL-005", "Temple Street",    "Fused",         RepairStatus.ASSIGNED, "Ravi Kumar (Lineman)","6 hrs ago"),
        Complaint("CMP-2837", "PL-007", "School Road",      "On in Daytime", RepairStatus.ASSIGNED, "Panchayat Office",    "3 hrs ago"),
        Complaint("CMP-2835", "PL-003", "Gandhi Nagar",     "On in Daytime", RepairStatus.PENDING,  "—",                   "30 min ago")
    )
}
