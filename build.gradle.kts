plugins {
    alias(libs.plugins.algomate)
}

exercise {
    assignmentId.set("h12")
}

submission {
    // ACHTUNG!
    // Setzen Sie im folgenden Bereich Ihre TU-ID (NICHT Ihre Matrikelnummer!), Ihren Nachnamen und Ihren Vornamen
    // in Anführungszeichen (z.B. "ab12cdef" für Ihre TU-ID) ein!
    // BEISPIEL:
    // studentId = "ab12cdef"
    // firstName = "sol_first"
    // lastName = "sol_last"
    studentId = "cs52mina"
    firstName = "CalinGeorge"
    lastName = "Sobaru"

    // Optionally require own tests for mainBuildSubmission task. Default is false
    requireTests = false
}

jagr {
    graders {
        val graderPublic by getting {
            rubricProviderName.set("h12.H12_RubricProviderPublic")
        }
    }
}
