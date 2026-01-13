<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa thông tin cá nhân - Vietnam Travel</title>
    
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700&family=Pacifico&display=swap" rel="stylesheet">
    
    <!-- Icon -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/CSS/HomePage.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/CSS/Auth.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/CSS/EditProfile.css" rel="stylesheet">
</head>
<body>
    <!-- Import Header -->
    <jsp:include page="/WEB-INF/Common/header.jsp" />

    <!-- Edit Profile Section -->
    <section class="edit-profile-section">
        <div class="edit-profile-card">
            <div class="profile-header">
                <div class="avatar-section">
                    <div class="avatar">
                        <i class="fas fa-user"></i>
                    </div>
                    <h1>Chỉnh sửa thông tin</h1>
                    <p>Cập nhật thông tin cá nhân của bạn</p>
                </div>
            </div>

            <!-- Tab Navigation -->
            <div class="tab-navigation">
                <button class="tab-btn ${activeTab == 'profile' ? 'active' : ''}" data-tab="profile">
                    <i class="fas fa-user"></i>
                    <span>Thông tin</span>
                </button>
                <button class="tab-btn ${activeTab == 'password' ? 'active' : ''}" data-tab="password">
                    <i class="fas fa-lock"></i>
                    <span>Đổi mật khẩu</span>
                </button>
            </div>

            <!-- Tab Content -->
            <div class="tab-content">
                <!-- Profile Info Tab -->
                <div class="tab-panel ${activeTab == 'profile' ? 'active' : ''}" id="profile-panel">
                    <form id="editProfileForm" action="${pageContext.request.contextPath}/Edit_Profile" method="post" class="profile-form">
                        <input type="hidden" name="userId" id="userId" value="${user.userId}">
                        
                        <!-- Row 1: Email and Role -->
                        <div class="form-row">
                            <div class="form-group">
                                <label for="email">
                                    <i class="fas fa-envelope"></i> Email
                                </label>
                                <input type="email" id="email" value="${user.email}" readonly disabled>
                                <small>Email không thể thay đổi</small>
                            </div>
                            <div class="form-group">
                                <label for="role">
                                    <i class="fas fa-shield-alt"></i> Tài khoản
                                </label>
                                <input type="text" id="role" value="${user.role eq 'ADMIN' ? 'Quản trị' : 'Người dùng'}" readonly disabled>
                                <small>Vai trò tài khoản</small>
                            </div>
                        </div>

                        <!-- Row 2: Full Name and Phone -->
                        <div class="form-row">
                            <div class="form-group">
                                <label for="fullName">
                                    <i class="fas fa-user"></i> Họ và tên <span class="required">*</span>
                                </label>
                                <input type="text" id="fullName" name="fullName" value="${user.fullName}" 
                                       placeholder="Nhập họ và tên" maxlength="100">
                                <small class="error-message" id="fullNameError"></small>
                            </div>
                            <div class="form-group">
                                <label for="phone">
                                    <i class="fas fa-phone"></i> Số điện thoại
                                </label>
                                <input type="tel" id="phone" name="phone" value="${user.phone}" 
                                       placeholder="Nhập số điện thoại" maxlength="11">
                                <small class="error-message" id="phoneError"></small>
                            </div>
                        </div>

                        <!-- Success/Error Messages -->
                        <div id="profileMessageBox" class="message-box" style="display: none;"></div>

                        <!-- Buttons -->
                        <div class="form-actions">
                            <a href="${pageContext.request.contextPath}/HomePage.jsp" class="btn-cancel">
                                <i class="fas fa-times"></i> Hủy
                            </a>
                            <button type="submit" class="btn-update" id="profileSubmitBtn">
                                <i class="fas fa-save"></i> Cập nhật
                            </button>
                        </div>
                    </form>
                </div>

                <!-- Change Password Tab -->
                <div class="tab-panel ${activeTab == 'password' ? 'active' : ''}" id="password-panel">
                    <form id="changePasswordForm" action="${pageContext.request.contextPath}/Edit_Profile" method="post" class="password-form">
                        <input type="hidden" name="action" value="changePassword">
                        
                        <div class="password-section">
                            <h3>
                                <i class="fas fa-shield-alt"></i>
                                Đổi mật khẩu
                            </h3>
                            <p class="password-description">Để bảo mật tài khoản, vui lòng sử dụng mật khẩu mạnh và không sử dụng chung với các tài khoản khác.</p>
                        </div>

                        <!-- Current Password -->
                        <div class="form-group">
                            <label for="currentPassword">
                                <i class="fas fa-lock"></i> Mật khẩu hiện tại <span class="required">*</span>
                            </label>
                            <div class="password-input-wrapper">
                                <input type="password" id="currentPassword" name="currentPassword" 
                                       placeholder="Nhập mật khẩu hiện tại" maxlength="50">
                                <button type="button" class="toggle-password" data-target="currentPassword">
                                    <i class="fas fa-eye"></i>
                                </button>
                            </div>
                            <small class="error-message" id="currentPasswordError"></small>
                        </div>

                        <!-- New Password -->
                        <div class="form-group">
                            <label for="newPassword">
                                <i class="fas fa-key"></i> Mật khẩu mới <span class="required">*</span>
                            </label>
                            <div class="password-input-wrapper">
                                <input type="password" id="newPassword" name="newPassword" 
                                       placeholder="Nhập mật khẩu mới (tối thiểu 6 ký tự)" maxlength="50">
                                <button type="button" class="toggle-password" data-target="newPassword">
                                    <i class="fas fa-eye"></i>
                                </button>
                            </div>
                            <small class="error-message" id="newPasswordError"></small>
                        </div>

                        <!-- Confirm Password -->
                        <div class="form-group">
                            <label for="confirmPassword">
                                <i class="fas fa-check-circle"></i> Xác nhận mật khẩu mới <span class="required">*</span>
                            </label>
                            <div class="password-input-wrapper">
                                <input type="password" id="confirmPassword" name="confirmPassword" 
                                       placeholder="Nhập lại mật khẩu mới" maxlength="50">
                                <button type="button" class="toggle-password" data-target="confirmPassword">
                                    <i class="fas fa-eye"></i>
                                </button>
                            </div>
                            <small class="error-message" id="confirmPasswordError"></small>
                        </div>

                        <!-- Password Strength Indicator -->
                        <div class="password-strength">
                            <div class="strength-bar">
                                <div class="strength-fill" id="strengthFill"></div>
                            </div>
                            <span class="strength-text" id="strengthText">Độ mạnh mật khẩu</span>
                        </div>

                        <!-- Success/Error Messages -->
                        <div id="passwordMessageBox" class="message-box" style="display: none;"></div>

                        <!-- Buttons -->
                        <div class="form-actions">
                            <a href="${pageContext.request.contextPath}/HomePage.jsp" class="btn-cancel">
                                <i class="fas fa-times"></i> Hủy
                            </a>
                            <button type="submit" class="btn-update" id="passwordSubmitBtn">
                                <i class="fas fa-key"></i> Đổi mật khẩu
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>

    <!-- JavaScript -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            console.log('Page loaded');
            
            // Tab switching
            const tabBtns = document.querySelectorAll('.tab-btn');
            const tabPanels = document.querySelectorAll('.tab-panel');

            tabBtns.forEach(function(btn) {
                btn.addEventListener('click', function() {
                    const tabId = this.getAttribute('data-tab');
                    
                    tabBtns.forEach(function(b) { b.classList.remove('active'); });
                    this.classList.add('active');
                    
                    tabPanels.forEach(function(panel) {
                        panel.classList.remove('active');
                        if (panel.id === tabId + '-panel') {
                            panel.classList.add('active');
                        }
                    });
                });
            });

            // Password visibility toggle
            const togglePasswordBtns = document.querySelectorAll('.toggle-password');
            togglePasswordBtns.forEach(function(btn) {
                btn.addEventListener('click', function() {
                    const targetId = this.getAttribute('data-target');
                    const input = document.getElementById(targetId);
                    const icon = this.querySelector('i');
                    
                    if (input.type === 'password') {
                        input.type = 'text';
                        icon.classList.remove('fa-eye');
                        icon.classList.add('fa-eye-slash');
                    } else {
                        input.type = 'password';
                        icon.classList.remove('fa-eye-slash');
                        icon.classList.add('fa-eye');
                    }
                });
            });

            // Password strength indicator
            const newPasswordInput = document.getElementById('newPassword');
            const strengthFill = document.getElementById('strengthFill');
            const strengthText = document.getElementById('strengthText');

            if (newPasswordInput) {
                newPasswordInput.addEventListener('input', function() {
                    const password = this.value;
                    let strength = 0;
                    let text = 'Độ mạnh mật khẩu';
                    let color = '#e9ecef';

                    if (password.length >= 6) strength += 1;
                    if (password.length >= 10) strength += 1;
                    if (/[a-z]/.test(password)) strength += 1;
                    if (/[A-Z]/.test(password)) strength += 1;
                    if (/[0-9]/.test(password)) strength += 1;
                    if (/[^a-zA-Z0-9]/.test(password)) strength += 1;

                    switch (strength) {
                        case 0:
                        case 1:
                            text = 'Yếu';
                            color = '#dc3545';
                            break;
                        case 2:
                        case 3:
                            text = 'Trung bình';
                            color = '#ffc107';
                            break;
                        case 4:
                        case 5:
                        case 6:
                            text = 'Mạnh';
                            color = '#28a745';
                            break;
                    }

                    if (password.length === 0) {
                        text = 'Độ mạnh mật khẩu';
                        color = '#e9ecef';
                    }

                    strengthFill.style.width = Math.min((strength / 6) * 100, 100) + '%';
                    strengthFill.style.backgroundColor = color;
                    strengthText.textContent = text;
                    strengthText.style.color = color === '#e9ecef' ? '#6c757d' : color;
                });
            }

            // Clear password errors on input
            ['currentPassword', 'newPassword', 'confirmPassword'].forEach(function(id) {
                const el = document.getElementById(id);
                if (el) {
                    el.addEventListener('input', function() {
                        const errorEl = document.getElementById(id + 'Error');
                        if (errorEl) errorEl.textContent = '';
                        this.classList.remove('error');
                    });
                }
            });

            // Profile Form Handler
            initProfileForm();
            
            // Password Form Handler
            initPasswordForm();
        });

        // Profile Form Functions
        function initProfileForm() {
            console.log('initProfileForm called');
            const form = document.getElementById('editProfileForm');
            const submitBtn = document.getElementById('profileSubmitBtn');
            const messageBox = document.getElementById('profileMessageBox');
            const fullNameInput = document.getElementById('fullName');
            const phoneInput = document.getElementById('phone');
            const userIdInput = document.getElementById('userId');

            function clearErrors() {
                document.getElementById('fullNameError').textContent = '';
                document.getElementById('phoneError').textContent = '';
                fullNameInput.classList.remove('error');
                phoneInput.classList.remove('error');
            }

            function showMessage(message, type) {
                messageBox.textContent = message;
                messageBox.className = 'message-box ' + type + ' show';
                messageBox.innerHTML = '<i class="fas fa-' + (type === 'success' ? 'check-circle' : 'exclamation-circle') + '"></i> ' + message;
                messageBox.style.display = 'flex';
                
                messageBox.scrollIntoView({ behavior: 'smooth', block: 'center' });
                
                setTimeout(function() {
                    messageBox.className = 'message-box';
                }, 5000);
            }

            function validateForm() {
                let isValid = true;
                clearErrors();

                const fullName = fullNameInput.value.trim();
                const phone = phoneInput.value.trim();

                console.log('validateForm - fullName:', fullName);
                console.log('validateForm - fullName length:', fullName.length);

                if (!fullName || fullName.length === 0) {
                    document.getElementById('fullNameError').textContent = 'Họ và tên không được để trống';
                    fullNameInput.classList.add('error');
                    isValid = false;
                } else if (fullName.length < 2) {
                    document.getElementById('fullNameError').textContent = 'Họ và tên phải có ít nhất 2 ký tự';
                    fullNameInput.classList.add('error');
                    isValid = false;
                } else if (fullName.length > 100) {
                    document.getElementById('fullNameError').textContent = 'Họ và tên không được vượt quá 100 ký tự';
                    fullNameInput.classList.add('error');
                    isValid = false;
                }

                if (phone && phone.length > 0) {
                    const phoneRegex = /^0[0-9]{9,10}$/;
                    if (!phoneRegex.test(phone)) {
                        document.getElementById('phoneError').textContent = 'Số điện thoại không hợp lệ';
                        phoneInput.classList.add('error');
                        isValid = false;
                    }
                }

                return isValid;
            }

            // Real-time validation
            fullNameInput.addEventListener('input', function() {
                console.log('fullName input:', this.value);
                document.getElementById('fullNameError').textContent = '';
                this.classList.remove('error');
            });

            phoneInput.addEventListener('input', function() {
                const value = this.value.trim();
                document.getElementById('phoneError').textContent = '';
                this.classList.remove('error');
            });

            form.addEventListener('submit', function(e) {
                e.preventDefault();
                
                console.log('=== FORM SUBMIT ===');
                console.log('fullNameInput.value:', fullNameInput.value);
                console.log('fullNameInput.value.length:', fullNameInput.value.length);

                if (!validateForm()) {
                    console.log('Client validation failed');
                    return false;
                }

                console.log('Client validation passed');

                submitBtn.disabled = true;
                submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';

                // Use URLSearchParams instead of FormData for proper parameter parsing
                const params = new URLSearchParams();
                params.append('userId', userIdInput.value);
                params.append('fullName', fullNameInput.value);
                params.append('phone', phoneInput.value);

                console.log('Params created:');
                console.log('  userId:', userIdInput.value);
                console.log('  fullName:', fullNameInput.value);
                console.log('  phone:', phoneInput.value);

                fetch('${pageContext.request.contextPath}/Edit_Profile', {
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                    },
                    body: params.toString()
                })
                .then(function(response) {
                    console.log('Response status:', response.status);
                    return response.json();
                })
                .then(function(data) {
                    console.log('Server response:', data);
                    if (data.status === 'success') {
                        showMessage(data.message, 'success');
                        setTimeout(function() {
                            if (data.redirect) {
                                window.location.href = data.redirect;
                            }
                        }, 1500);
                    } else {
                        showMessage(data.message, 'error');
                        submitBtn.disabled = false;
                        submitBtn.innerHTML = '<i class="fas fa-save"></i> Cập nhật';
                    }
                })
                .catch(function(error) {
                    console.error('Fetch error:', error);
                    showMessage('Có lỗi xảy ra. Vui lòng thử lại.', 'error');
                    submitBtn.disabled = false;
                    submitBtn.innerHTML = '<i class="fas fa-save"></i> Cập nhật';
                });

                return false;
            });
        }

        // Password Form Functions
        function initPasswordForm() {
            const form = document.getElementById('changePasswordForm');
            const submitBtn = document.getElementById('passwordSubmitBtn');
            const messageBox = document.getElementById('passwordMessageBox');

            if (!form || !submitBtn) return;

            function showMessage(message, type) {
                messageBox.textContent = message;
                messageBox.className = 'message-box ' + type + ' show';
                messageBox.innerHTML = '<i class="fas fa-' + (type === 'success' ? 'check-circle' : 'exclamation-circle') + '"></i> ' + message;
                messageBox.style.display = 'flex';
                
                messageBox.scrollIntoView({ behavior: 'smooth', block: 'center' });
                
                setTimeout(function() {
                    messageBox.className = 'message-box';
                }, 5000);
            }

            function validateForm() {
                let isValid = true;
                
                document.getElementById('currentPasswordError').textContent = '';
                document.getElementById('newPasswordError').textContent = '';
                document.getElementById('confirmPasswordError').textContent = '';
                
                document.getElementById('currentPassword').classList.remove('error');
                document.getElementById('newPassword').classList.remove('error');
                document.getElementById('confirmPassword').classList.remove('error');

                const currentPassword = document.getElementById('currentPassword').value;
                const newPassword = document.getElementById('newPassword').value;
                const confirmPassword = document.getElementById('confirmPassword').value;

                if (!currentPassword) {
                    document.getElementById('currentPasswordError').textContent = 'Mật khẩu hiện tại không được để trống';
                    document.getElementById('currentPassword').classList.add('error');
                    isValid = false;
                }

                if (!newPassword) {
                    document.getElementById('newPasswordError').textContent = 'Mật khẩu mới không được để trống';
                    document.getElementById('newPassword').classList.add('error');
                    isValid = false;
                } else if (newPassword.length < 6) {
                    document.getElementById('newPasswordError').textContent = 'Mật khẩu mới phải có ít nhất 6 ký tự';
                    document.getElementById('newPassword').classList.add('error');
                    isValid = false;
                } else if (newPassword.length > 50) {
                    document.getElementById('newPasswordError').textContent = 'Mật khẩu mới không được vượt quá 50 ký tự';
                    document.getElementById('newPassword').classList.add('error');
                    isValid = false;
                }

                if (!confirmPassword) {
                    document.getElementById('confirmPasswordError').textContent = 'Xác nhận mật khẩu không được để trống';
                    document.getElementById('confirmPassword').classList.add('error');
                    isValid = false;
                } else if (newPassword !== confirmPassword) {
                    document.getElementById('confirmPasswordError').textContent = 'Mật khẩu xác nhận không khớp';
                    document.getElementById('confirmPassword').classList.add('error');
                    isValid = false;
                }

                return isValid;
            }

            form.addEventListener('submit', function(e) {
                e.preventDefault();

                if (!validateForm()) {
                    return false;
                }

                submitBtn.disabled = true;
                submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';

                // Use URLSearchParams instead of FormData for proper parameter parsing
                const params = new URLSearchParams();
                params.append('action', 'changePassword');
                params.append('currentPassword', document.getElementById('currentPassword').value);
                params.append('newPassword', document.getElementById('newPassword').value);
                params.append('confirmPassword', document.getElementById('confirmPassword').value);

                fetch('${pageContext.request.contextPath}/Edit_Profile', {
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                    },
                    body: params.toString()
                })
                .then(function(response) { return response.json(); })
                .then(function(data) {
                    if (data.status === 'success') {
                        showMessage(data.message, 'success');
                        setTimeout(function() {
                        	window.location.href = '${pageContext.request.contextPath}/HomePage.jsp';
                            document.getElementById('strengthFill').style.width = '0%';
                            document.getElementById('strengthText').textContent = 'Độ mạnh mật khẩu';
                            document.getElementById('strengthText').style.color = '#6c757d';
                        }, 1500);
                    } else {
                        showMessage(data.message, 'error');
                        submitBtn.disabled = false;
                        submitBtn.innerHTML = '<i class="fas fa-key"></i> Đổi mật khẩu';
                    }
                })
                .catch(function(error) {
                    console.error('Error:', error);
                    showMessage('Có lỗi xảy ra. Vui lòng thử lại.', 'error');
                    submitBtn.disabled = false;
                    submitBtn.innerHTML = '<i class="fas fa-key"></i> Đổi mật khẩu';
                });

                return false;
            });
        }
    </script>
</body>
</html>
