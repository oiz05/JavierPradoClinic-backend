-- Supabase/PostgreSQL support objects for appointment availability.
-- Run manually if the target database was created before these constraints/indexes existed.
-- doctor_schedules.day_of_week must use ISO values: Monday=1, Tuesday=2, ..., Sunday=7.
-- Audit before applying the constraint: select id, day_of_week from doctor_schedules where day_of_week not between 1 and 7;

create index if not exists idx_doctor_schedules_lookup
    on doctor_schedules (doctor_profile_id, clinic_id, day_of_week, active, start_time);

create index if not exists idx_appointments_availability_lookup
    on appointments (doctor_profile_id, clinic_id, appointment_date, status);

create index if not exists idx_appointments_type_lookup
    on appointments (appointment_type_id);

do $$
begin
    if not exists (
        select 1 from pg_constraint where conname = 'ck_doctor_schedules_day_of_week'
    ) then
        alter table doctor_schedules
            add constraint ck_doctor_schedules_day_of_week
            check (day_of_week between 1 and 7);
    end if;

    if not exists (
        select 1 from pg_constraint where conname = 'ck_doctor_schedules_time_range'
    ) then
        alter table doctor_schedules
            add constraint ck_doctor_schedules_time_range
            check (start_time < end_time);
    end if;
end $$;
