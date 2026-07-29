package com.atlanta.banking.identity.service.utils.seeder;

import com.atlanta.banking.identity.service.enums.Department;
import com.atlanta.banking.identity.service.enums.Designation;

import java.util.List;
import java.util.Set;

public final class SeedData {

    public static final String[][] INITIAL_ROLES = {{"ROLE_SUPER_ADMIN", "Unrestricted system-wide access across all microservices configurations and root settings"}, {"ROLE_ADMIN", "System administrator responsible for employee lifecycle management and service settings"}, {"ROLE_SECURITY_ADMIN", "Security officer managing RBAC permissions key rotation access policies and security alerts"}, {"ROLE_IT_SUPPORT", "Helpdesk specialist handling password resets session terminations and account unlock requests"}, {"ROLE_TELLER", "Front-desk associate performing basic cash deposits withdrawals and balance inquiries"}, {"ROLE_HEAD_TELLER", "Vault custodian managing cash drawer balances till reconciliations and teller overrides"}, {"ROLE_CUSTOMER_SERVICE_REP", "Branch representative opening customer accounts issuing debit cards and updating profiles"}, {"ROLE_BRANCH_MANAGER", "Senior branch authority overseeing daily operations high-value overrides and staff performance"}, {"ROLE_ASSISTANT_BRANCH_MANAGER", "Deputy manager providing supervisory support secondary approvals and operational continuity"}, {"ROLE_LOAN_OFFICER", "Credit specialist originating evaluating and processing personal and commercial loan applications"}, {"ROLE_LOAN_APPROVER", "Underwriting authority evaluating risk models and granting final approval on high-value loans"}, {"ROLE_CARD_SPECIALIST", "Operations staff managing credit/debit card issuance block/unblock requests and limit updates"}, {"ROLE_TREASURY_ANALYST", "Financial analyst managing liquidity capital reserves foreign exchange and interbank transfers"}, {"ROLE_CORPORATE_BANKER", "Relationship manager handling high-net-worth commercial accounts payrolls and corporate credit lines"}, {"ROLE_COMPLIANCE_OFFICER", "Regulatory officer monitoring anti-money laundering (AML) KYC verification and transaction limits"}, {"ROLE_AUDITOR_INTERNAL", "Internal inspector with read-only access to transaction ledgers system logs and employee actions"}, {"ROLE_AUDITOR_EXTERNAL", "External compliance auditor assigned temporary strictly read-only access for periodic regulatory reviews"}, {"ROLE_FRAUD_ANALYST", "Risk specialist investigating suspicious transaction flags freezing compromised accounts and filing SARs"}};
    public static final List<SeedEmployee> INITIAL_EMPLOYEES = List.of(

            new SeedEmployee("Aman", "Jaiswal", "aman.jaiswal@atlantabank.com", "9000000001", Department.IT, Designation.SUPER_ADMIN, "SuperAdmin@123", Set.of("ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_SECURITY_ADMIN")),

            new SeedEmployee("Sarah", "Mitchell", "sarah.mitchell@atlantabank.com", "9000000002", Department.IT, Designation.ADMIN, "Admin@123", Set.of("ROLE_ADMIN")),

            new SeedEmployee("Ethan", "Brooks", "ethan.brooks@atlantabank.com", "9000000003", Department.IT, Designation.MANAGER, "Manager@123", Set.of("ROLE_IT_SUPPORT")),

            new SeedEmployee("Michael", "Carter", "michael.carter@atlantabank.com", "9000000004", Department.RETAIL_BANKING, Designation.MANAGER, "Manager@123", Set.of("ROLE_BRANCH_MANAGER")),

            new SeedEmployee("Emily", "Rodriguez", "emily.rodriguez@atlantabank.com", "9000000005", Department.RETAIL_BANKING, Designation.ASSISTANT_MANAGER, "Manager@123", Set.of("ROLE_ASSISTANT_BRANCH_MANAGER")),

            new SeedEmployee("David", "Kim", "david.kim@atlantabank.com", "9000000006", Department.RETAIL_BANKING, Designation.OFFICER, "Officer@123", Set.of("ROLE_TELLER")),

            new SeedEmployee("Olivia", "Brown", "olivia.brown@atlantabank.com", "9000000007", Department.RETAIL_BANKING, Designation.SENIOR_OFFICER, "Officer@123", Set.of("ROLE_HEAD_TELLER")),

            new SeedEmployee("James", "Wilson", "james.wilson@atlantabank.com", "9000000008", Department.OPERATIONS, Designation.OFFICER, "Officer@123", Set.of("ROLE_CUSTOMER_SERVICE_REP")),

            new SeedEmployee("Sophia", "Davis", "sophia.davis@atlantabank.com", "9000000009", Department.LOANS, Designation.MANAGER, "Manager@123", Set.of("ROLE_LOAN_OFFICER")),

            new SeedEmployee("Daniel", "Anderson", "daniel.anderson@atlantabank.com", "9000000010", Department.LOANS, Designation.SENIOR_MANAGER, "Manager@123", Set.of("ROLE_LOAN_APPROVER")),

            new SeedEmployee("Ethan", "Thomas", "ethan.thomas@atlantabank.com", "9000000011", Department.CARDS, Designation.OFFICER, "Officer@123", Set.of("ROLE_CARD_SPECIALIST")),

            new SeedEmployee("Grace", "Lee", "grace.lee@atlantabank.com", "9000000012", Department.FINANCE, Designation.SENIOR_MANAGER, "Manager@123", Set.of("ROLE_TREASURY_ANALYST")),

            new SeedEmployee("Benjamin", "Hall", "benjamin.hall@atlantabank.com", "9000000013", Department.CORPORATE_BANKING, Designation.MANAGER, "Manager@123", Set.of("ROLE_CORPORATE_BANKER")),

            new SeedEmployee("Chloe", "Martin", "chloe.martin@atlantabank.com", "9000000014", Department.OPERATIONS, Designation.SENIOR_MANAGER, "Manager@123", Set.of("ROLE_COMPLIANCE_OFFICER")),

            new SeedEmployee("Noah", "Harris", "noah.harris@atlantabank.com", "9000000015", Department.OPERATIONS, Designation.SENIOR_MANAGER, "Manager@123", Set.of("ROLE_FRAUD_ANALYST", "ROLE_AUDITOR_INTERNAL")));

    private SeedData() {
    }
}
